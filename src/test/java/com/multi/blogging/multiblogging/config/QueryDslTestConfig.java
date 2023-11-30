package com.multi.blogging.multiblogging.config;

import com.multi.blogging.multiblogging.auth.repository.custom.CustomMemberRepository;
import com.multi.blogging.multiblogging.auth.repository.custom.impl.CustomMemberRepositoryImpl;
import com.multi.blogging.multiblogging.board.repository.custom.CustomBoardRepository;
import com.multi.blogging.multiblogging.board.repository.custom.impl.CustomBoardRepositoryImpl;
import com.multi.blogging.multiblogging.category.repository.custom.CustomCategoryRepository;
import com.multi.blogging.multiblogging.category.repository.custom.impl.CustomCategoryRepositoryImpl;
import com.multi.blogging.multiblogging.comment.repository.custom.CustomCommentRepository;
import com.multi.blogging.multiblogging.comment.repository.custom.CustomReCommentRepository;
import com.multi.blogging.multiblogging.comment.repository.custom.impl.CustomCommentRepositoryImpl;
import com.multi.blogging.multiblogging.comment.repository.custom.impl.CustomReCommentRepositoryImpl;
import com.multi.blogging.multiblogging.heart.repository.custom.CustomHeartRepository;
import com.multi.blogging.multiblogging.heart.repository.custom.impl.CustomHeartRepositoryImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class QueryDslTestConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public CustomMemberRepository customMemberRepository(){
        return new CustomMemberRepositoryImpl(jpaQueryFactory());
    }

    @Bean
    public CustomCategoryRepository customCategoryRepository(){
        return new CustomCategoryRepositoryImpl(jpaQueryFactory());
    }

    @Bean
    public CustomHeartRepository customHeartRepository(){
        return new CustomHeartRepositoryImpl(jpaQueryFactory());
    }

    @Bean
    public CustomBoardRepository customBoardRepository(){
        return new CustomBoardRepositoryImpl(jpaQueryFactory());
    }

    @Bean
    public CustomCommentRepository customCommentRepository(){
        return new CustomCommentRepositoryImpl(jpaQueryFactory());
    }

    @Bean
    public CustomReCommentRepository customReCommentRepository(){
        return new CustomReCommentRepositoryImpl(jpaQueryFactory());
    }
}