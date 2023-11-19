package com.multi.blogging.multiblogging.comment.interceptor;

import com.multi.blogging.multiblogging.base.SecurityUtil;
import com.multi.blogging.multiblogging.comment.domain.Comment;
import com.multi.blogging.multiblogging.comment.exception.CommentNotFoundException;
import com.multi.blogging.multiblogging.comment.exception.CommentPermissionDeniedException;
import com.multi.blogging.multiblogging.comment.repository.CommentRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CommentAuthInterceptor implements HandlerInterceptor {

    private final CommentRepository commentRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String httpMethod = request.getMethod();
        if (httpMethod.equals("PUT") || httpMethod.equals("DELETE")) {
            Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            Long commentId = Long.parseLong((String) pathVariables.get("comment_id"));
            Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
            if (!comment.getMember().getEmail().equals(SecurityUtil.getCurrentMemberEmail())) {
                throw new CommentPermissionDeniedException();
            }

        }
        return true;

    }
}