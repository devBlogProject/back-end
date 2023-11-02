package com.multi.blogging.multiblogging.board.service;

import com.multi.blogging.multiblogging.board.domain.Board;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    static final String DEFAULT_THUMB_URL = "https://cdn.pixabay.com/photo/2020/11/08/13/28/tree-5723734_1280.jpg";


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
