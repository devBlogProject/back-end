package com.multi.blogging.multiblogging.board.service;

import com.multi.blogging.multiblogging.board.domain.Board;
import com.multi.blogging.multiblogging.board.dto.request.BoardRequestDto;
import com.multi.blogging.multiblogging.board.exception.BoardNotFoundException;
import com.multi.blogging.multiblogging.board.exception.BoardTitleConflictException;
import com.multi.blogging.multiblogging.board.repository.BoardRepository;
import com.multi.blogging.multiblogging.category.domain.Category;
import com.multi.blogging.multiblogging.category.exception.CategoryAccessPermissionDeniedException;
import com.multi.blogging.multiblogging.category.exception.CategoryNotFoundException;
import com.multi.blogging.multiblogging.category.repository.CategoryRepository;
import com.multi.blogging.multiblogging.imageUpload.service.ImageUploadService;
import com.multi.blogging.multiblogging.infra.redisDb.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final CategoryRepository categoryRepository;
    private final BoardRepository boardRepository;
    private final ImageUploadService imageUploadService;
    private final RedisService redisService;

    public final String VIEW_COUNT_PREFIX = "Viewcount ";
    public static final String DEFAULT_THUMB_URL = "https://cdn.pixabay.com/photo/2020/11/08/13/28/tree-5723734_1280.jpg";


    @Transactional(readOnly = true)
    public Board getBoard(HttpServletRequest request, String nickname, int postNum) {
        Board board = boardRepository.findByMemberNicknameAndPostNumberWithMember(nickname, postNum).orElseThrow(BoardNotFoundException::new);
        addViewCount(request, board);
        return board;
    }

    @Scheduled(fixedDelay = 1000 * 60 * 30) //30분
    @Transactional
    public void transferAndClearViewCount() {
        Map<String, Set> boardIdAndAddressMap = redisService.getKeyAndSetOpsContainPrefix(VIEW_COUNT_PREFIX);
        for (String key : boardIdAndAddressMap.keySet()) {
            Set IPAddresses = boardIdAndAddressMap.get(key);
            Long boardId = Long.valueOf(key.replace(VIEW_COUNT_PREFIX, ""));
            Optional<Board> board=boardRepository.findById(boardId);
            if (board.isPresent()){
                int oldViewCount = board.get().getViewCount();
                int newViewCount = oldViewCount + IPAddresses.size();
                board.get().setViewCount(newViewCount);
            }
        }
        redisService.deleteKeyByContainPrefix(VIEW_COUNT_PREFIX);
    }

    private void addViewCount(HttpServletRequest request, Board board) {
        String ipAddress = request.getRemoteAddr();
        redisService.setSetOps(VIEW_COUNT_PREFIX + board.getId(), ipAddress);
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        var board = boardRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);
        boardRepository.delete(board);
    }

    @Transactional
    public Board updateBoard(Long boardId, BoardRequestDto boardRequestDto, MultipartFile thumbNailImage) {
        Board board = boardRepository.findByIdWithCategoryAndMember(boardId).orElseThrow(BoardNotFoundException::new);
        if (thumbNailImage != null) {
            String thumbnailUrl = uploadImage(thumbNailImage);
            board.setThumbnailUrl(thumbnailUrl);
        }
        if (board.getCategory() != null) {
            Category categoryHavingBoard = categoryRepository.findByIdWithMemberAndBoard(board.getCategory().getId()).orElseThrow(CategoryNotFoundException::new);
            if (isDuplicateTitleInCategory(categoryHavingBoard, boardRequestDto.getTitle())) {
                throw new BoardTitleConflictException();
            }
        }

        board.setTitle(boardRequestDto.getTitle());
        board.setContent(boardRequestDto.getContent());
        if (!Objects.equals(boardRequestDto.getCategoryId(), board.getCategory().getId())) {
            Category category = categoryRepository.findById(boardRequestDto.getCategoryId()).orElseThrow(CategoryNotFoundException::new);
            board.changeCategory(category);
        }
        return board;
    }

    @Transactional(readOnly = true)
    public Slice<Board> getBoardSlice(Pageable pageable) {
        return boardRepository.findSliceWithMember(pageable);
    }

    @Transactional(readOnly = true)
    public Slice<Board> getNicknameBoardSlice(Pageable pageable, String nickname){
        return boardRepository.findSliceByNicknameWithMember(pageable,nickname);
    }

    @Transactional
    public Board writeBoard(BoardRequestDto requestDto, MultipartFile thumbNailImage, String memberEmail) {
        var category = categoryRepository.findByIdWithMemberAndBoard(requestDto.getCategoryId()).orElseThrow(CategoryNotFoundException::new);
        int postNum = category.getMember().getBoardList().size() + 1;
        if (!hasPermissionOfCategory(category, memberEmail)) {
            throw new CategoryAccessPermissionDeniedException();
        }
        if (isDuplicateTitleInCategory(category, requestDto.getTitle())) {
            throw new BoardTitleConflictException();
        }

        String thumbnailUrl = makeThumbnailUrl(thumbNailImage, requestDto.getContent());

        Board board = Board.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .thumbnailUrl(thumbnailUrl)
                .postNumber(postNum)
                .build();
        board.changeAuthor(category.getMember());
        board.changeCategory(category);
        return boardRepository.save(board);
    }

    public String uploadImage(MultipartFile image) {
        return imageUploadService.uploadFile(image);
    }


    private String makeThumbnailUrl(MultipartFile thumbnailImage, String content) {
        String thumbnailUrl;
        if (thumbnailImage != null) {
            thumbnailUrl = imageUploadService.uploadFile(thumbnailImage);
        } else {
            thumbnailUrl = makeDefaultThumb(content);
        }
        return thumbnailUrl;
    }

    private boolean hasPermissionOfCategory(Category category, String memberEmail) {
        return category.getMember().getEmail().equals(memberEmail);
    }

    private boolean isDuplicateTitleInCategory(Category category, String boardTitle) {
        return category.getBoards().stream().anyMatch((board1 -> board1.getTitle().equals(boardTitle)));
    }

    private String makeDefaultThumb(String content) {
        Document doc = Jsoup.parse(content);

        // "img" 태그를 모두 선택합니다.
        Elements images = doc.select("img");

        if (images.isEmpty()) {
            return DEFAULT_THUMB_URL;
        } else {
            return images.get(0).attr("src");
        }
    }
}
