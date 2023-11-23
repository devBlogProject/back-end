package com.multi.blogging.multiblogging.board.service;

import com.multi.blogging.multiblogging.auth.exception.MemberNotFoundException;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import com.multi.blogging.multiblogging.base.SecurityUtil;
import com.multi.blogging.multiblogging.board.domain.Board;
import com.multi.blogging.multiblogging.board.dto.request.BoardRequestDto;
import com.multi.blogging.multiblogging.board.exception.BoardNotFoundException;
import com.multi.blogging.multiblogging.board.exception.BoardPermissionDeniedException;
import com.multi.blogging.multiblogging.board.exception.BoardTitleConflictException;
import com.multi.blogging.multiblogging.board.repository.BoardRepository;
import com.multi.blogging.multiblogging.category.domain.Category;
import com.multi.blogging.multiblogging.category.exception.CategoryAccessPermissionDeniedException;
import com.multi.blogging.multiblogging.category.exception.CategoryNotFoundException;
import com.multi.blogging.multiblogging.category.repository.CategoryRepository;
import com.multi.blogging.multiblogging.imageUpload.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final CategoryRepository categoryRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final ImageUploadService imageUploadService;
    public static final String DEFAULT_THUMB_URL = "https://cdn.pixabay.com/photo/2020/11/08/13/28/tree-5723734_1280.jpg";


    @Transactional(readOnly = true)
    public Board getBoard(Long boardId){
        return boardRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);
    }

    @Transactional
    public void deleteBoard(Long boardId){
        var board = boardRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);
        boardRepository.delete(board);
    }

    @Transactional
    public Board updateBoard(Long boardId,BoardRequestDto boardRequestDto,MultipartFile thumbNailImage){
        Board board = boardRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);
        if (thumbNailImage!=null){
            String thumbnailUrl = uploadImage(thumbNailImage);
            board.setThumbnailUrl(thumbnailUrl);
        }
        if (board.getCategory()!=null){
            if (isDuplicateTitleInCategory(board.getCategory(), boardRequestDto.getTitle())){
                throw new BoardTitleConflictException();
            }
        }

        board.setTitle(boardRequestDto.getTitle());
        board.setContent(boardRequestDto.getContent());
        if (boardRequestDto.getCategoryId()!=board.getCategory().getId()){
            Category category = categoryRepository.findById(boardRequestDto.getCategoryId()).orElseThrow(CategoryNotFoundException::new);
            board.changeCategory(category);
        }
        return board;
    }

    @Transactional(readOnly = true)
    public Slice<Board> getBoards(Pageable pageable){
        return boardRepository.findSliceBy(pageable);
    }

    @Transactional
    public Board writeBoard(BoardRequestDto requestDto,MultipartFile thumbNailImage){
        var category = categoryRepository.findById(requestDto.getCategoryId()).orElseThrow(CategoryNotFoundException::new);
        if (!hasPermissionOfCategory(category)){
            throw new CategoryAccessPermissionDeniedException();
        }
        if (isDuplicateTitleInCategory(category, requestDto.getTitle())){
            throw new BoardTitleConflictException();
        }
        var author = memberRepository.findOneByEmail(SecurityUtil.getCurrentMemberEmail()).orElseThrow(MemberNotFoundException::new);

        String thumbnailUrl = makeThumbnailUrl(thumbNailImage, requestDto.getContent());

        Board board = Board.builder()
                .author(author)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .thumbnailUrl(thumbnailUrl)
                .category(category)
                .build();

    return boardRepository.save(board);
    }

    public String uploadImage(MultipartFile image){
        return imageUploadService.uploadFile(image);
    }


    private String makeThumbnailUrl(MultipartFile thumbnailImage, String content){
        String thumbnailUrl;
        if (thumbnailImage!=null){
            thumbnailUrl = imageUploadService.uploadFile(thumbnailImage);
        }else{
            thumbnailUrl = makeDefaultThumb(content);
        }
        return thumbnailUrl;
    }
    private boolean hasPermissionOfCategory(Category category){
        return category.getMember().getEmail().equals(SecurityUtil.getCurrentMemberEmail());
    }

    private boolean isDuplicateTitleInCategory(Category category,String boardTitle){
        return category.getBoards().stream().anyMatch((board1 -> board1.getTitle().equals(boardTitle)));
    }

    private String makeDefaultThumb(String content) {
        Document doc = Jsoup.parse(content);

        // "img" 태그를 모두 선택합니다.
        Elements images = doc.select("img");

       if (images.isEmpty()){
           return DEFAULT_THUMB_URL;
       }else{
           return images.get(0).attr("src");
       }
    }
}
