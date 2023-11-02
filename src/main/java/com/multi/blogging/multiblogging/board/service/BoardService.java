package com.multi.blogging.multiblogging.board.service;

import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import com.multi.blogging.multiblogging.base.SecurityUtil;
import com.multi.blogging.multiblogging.board.domain.Board;
import com.multi.blogging.multiblogging.board.dto.request.BoardRequestDto;
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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final CategoryRepository categoryRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final ImageUploadService imageUploadService;
    static final String DEFAULT_THUMB_URL = "https://cdn.pixabay.com/photo/2020/11/08/13/28/tree-5723734_1280.jpg";


    public Board writeBoard(Long categoryId,BoardRequestDto requestDto){
        var category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        if (!hasAuthOfCategory(category)){
            throw new CategoryAccessPermissionDeniedException();
        }
        var author = memberRepository.findOneByEmail(SecurityUtil.getCurrentMemberEmail()).get();

        String thumbnailUrl;
        if (requestDto.getThumbnailPicture()!=null){
        thumbnailUrl = imageUploadService.uploadFile(requestDto.getThumbnailPicture());
        }

        Board board = Board.builder()
                .author(author)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
//                .thumbnailUrl(thumbnailUrl)
                .category(category)
                .build();
    return board;  // !!! (수정필요, 커밋을 위해 임시로 만든 코드)


    }

    private boolean hasAuthOfCategory(Category category){
        return category.getMember().getEmail().equals(SecurityUtil.getCurrentMemberEmail());
    }
    private String makeDefaultThumb(Board board,String content) {
        Document doc = Jsoup.parse(content);

        // "img" 태그를 모두 선택합니다.
        Elements images = doc.select("img");

        if ((board.getThumbnailUrl().isBlank())) {
            if (images.isEmpty())
                board.setThumbnailUrl(DEFAULT_THUMB_URL);
            else
                board.setThumbnailUrl(images.get(0).attr("img"));
        }
        return board.getThumbnailUrl();
    }
}
