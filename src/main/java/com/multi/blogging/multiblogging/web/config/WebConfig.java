package com.multi.blogging.multiblogging.web.config;

import com.multi.blogging.multiblogging.board.interceptor.BoardAuthInterceptor;
import com.multi.blogging.multiblogging.comment.interceptor.CommentAuthInterceptor;
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
    private final BoardAuthInterceptor boardAuthInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
        ;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(commentAuthInterceptor)
                .addPathPatterns("/comment/**");

        registry.addInterceptor(boardAuthInterceptor)
                .addPathPatterns("/board/**");
    }

}
