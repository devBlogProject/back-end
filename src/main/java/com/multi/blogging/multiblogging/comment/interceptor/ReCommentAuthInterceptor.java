package com.multi.blogging.multiblogging.comment.interceptor;

import com.multi.blogging.multiblogging.base.SecurityUtil;
import com.multi.blogging.multiblogging.comment.domain.Comment;
import com.multi.blogging.multiblogging.comment.domain.ReComment;
import com.multi.blogging.multiblogging.comment.exception.CommentNotFoundException;
import com.multi.blogging.multiblogging.comment.exception.CommentPermissionDeniedException;
import com.multi.blogging.multiblogging.comment.repository.CommentRepository;
import com.multi.blogging.multiblogging.comment.repository.ReCommentRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReCommentAuthInterceptor implements HandlerInterceptor {

    private final ReCommentRepository reCommentRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String httpMethod = request.getMethod();
        if (httpMethod.equals("PATCH") || httpMethod.equals("DELETE")) {
            Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            Long commentId = Long.parseLong((String) pathVariables.get("id"));
            ReComment reComment = reCommentRepository.findByIdWithMember(commentId).orElseThrow(CommentNotFoundException::new);
            if (!reComment.getMember().getEmail().equals(SecurityUtil.getCurrentMemberEmail())) {
                throw new CommentPermissionDeniedException();
            }

        }
        return true;

    }
}