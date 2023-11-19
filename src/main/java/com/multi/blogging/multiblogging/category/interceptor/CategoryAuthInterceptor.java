package com.multi.blogging.multiblogging.category.interceptor;

import com.multi.blogging.multiblogging.base.SecurityUtil;
import com.multi.blogging.multiblogging.category.exception.CategoryAccessPermissionDeniedException;
import com.multi.blogging.multiblogging.category.exception.CategoryNotFoundException;
import com.multi.blogging.multiblogging.category.repository.CategoryRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CategoryAuthInterceptor implements HandlerInterceptor {
    private final CategoryRepository categoryRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String httpMethod = request.getMethod();
        Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if (httpMethod.equals("PATCH") || httpMethod.equals("DELETE")) {
            Long categoryId = Long.parseLong((String) pathVariables.get("category_id"));
            var category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
            if (!category.getMember().getEmail().equals(SecurityUtil.getCurrentMemberEmail())) {
                throw new CategoryAccessPermissionDeniedException();
            }
        }else if (httpMethod.equals("POST")&& existPathVariable(request,"/category/{parent_id}")){
            Long parentId = Long.parseLong((String) pathVariables.get("parent_id"));
            var parentCategory = categoryRepository.findById(parentId).orElseThrow(CategoryAccessPermissionDeniedException::new);
            if (!parentCategory.getMember().getEmail().equals(SecurityUtil.getCurrentMemberEmail())){
                throw new CategoryAccessPermissionDeniedException();
            }
        }
        return true;
    }

    private boolean existPathVariable(HttpServletRequest request,String pattern){
        String uri = request.getRequestURI();
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(pattern, uri);
    }
}
