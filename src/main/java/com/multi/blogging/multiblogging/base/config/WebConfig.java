package com.multi.blogging.multiblogging.base.config;

import com.multi.blogging.multiblogging.board.interceptor.BoardAuthInterceptor;
import com.multi.blogging.multiblogging.category.interceptor.CategoryAuthInterceptor;
import com.multi.blogging.multiblogging.comment.interceptor.CommentAuthInterceptor;
import com.multi.blogging.multiblogging.comment.interceptor.ReCommentAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final CommentAuthInterceptor commentAuthInterceptor;
    private final ReCommentAuthInterceptor reCommentAuthInterceptor;
    private final BoardAuthInterceptor boardAuthInterceptor;
    private final CategoryAuthInterceptor categoryAuthInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET","POST","PUT","DELETE","PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
        ;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(commentAuthInterceptor)
                .addPathPatterns("/comment/**");
        registry.addInterceptor(reCommentAuthInterceptor)
                        .addPathPatterns("/re-comment/**");

        registry.addInterceptor(boardAuthInterceptor)
                .addPathPatterns("/board/**");

        registry.addInterceptor(categoryAuthInterceptor)
                .addPathPatterns("/category/**");
    }

}
