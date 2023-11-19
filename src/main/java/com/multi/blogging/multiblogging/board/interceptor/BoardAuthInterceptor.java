package com.multi.blogging.multiblogging.board.interceptor;

import com.multi.blogging.multiblogging.base.SecurityUtil;
import com.multi.blogging.multiblogging.board.domain.Board;
import com.multi.blogging.multiblogging.board.exception.BoardNotFoundException;
import com.multi.blogging.multiblogging.board.exception.BoardPermissionDeniedException;
import com.multi.blogging.multiblogging.board.repository.BoardRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class BoardAuthInterceptor implements HandlerInterceptor {
    private final BoardRepository boardRepository;

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
        }else if (httpMethod.equals("POST")){

        }

        return true;
    }
}
