package com.multi.blogging.multiblogging.board.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multi.blogging.multiblogging.base.SecurityUtil;
import com.multi.blogging.multiblogging.board.domain.Board;
import com.multi.blogging.multiblogging.board.dto.request.BoardRequestDto;
import com.multi.blogging.multiblogging.board.exception.BoardNotFoundException;
import com.multi.blogging.multiblogging.board.exception.BoardPermissionDeniedException;
import com.multi.blogging.multiblogging.board.repository.BoardRepository;
import com.multi.blogging.multiblogging.category.exception.CategoryAccessPermissionDeniedException;
import com.multi.blogging.multiblogging.category.exception.CategoryNotFoundException;
import com.multi.blogging.multiblogging.category.repository.CategoryRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.data.web.JsonPath;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BoardAuthInterceptor implements HandlerInterceptor {
    private final BoardRepository boardRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        String httpMethod = request.getMethod();
        if(httpMethod.equals("PUT") || httpMethod.equals("DELETE")){
            Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            Long boardId = Long.parseLong((String) pathVariables.get("board_id"));

            Board board = boardRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);

            if (!board.getAuthor().getEmail().equals(SecurityUtil.getCurrentMemberEmail())){
                throw new BoardPermissionDeniedException();
            }
        }
//        else if (httpMethod.equals("POST")){
//            MultipartFile dtoFile = (MultipartFile) request.getPart("boardRequestDto");
//
//            request.get
//            BoardRequestDto dto = objectMapper.readValue(Arrays.toString(dtoFile.getBytes()), BoardRequestDto.class);
//            var category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(CategoryNotFoundException::new);
//
//            if(!category.getMember().getEmail().equals(SecurityUtil.getCurrentMemberEmail())){
//                throw new CategoryAccessPermissionDeniedException();
//            }
//        }

        return true;
    }
}
