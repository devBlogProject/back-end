package com.multi.blogging.multiblogging.config;

import com.multi.blogging.multiblogging.auth.repository.custom.CustomMemberRepository;
import com.multi.blogging.multiblogging.auth.repository.custom.impl.CustomMemberRepositoryImpl;
import com.multi.blogging.multiblogging.category.repository.custom.CustomCategoryRepository;
import com.multi.blogging.multiblogging.category.repository.custom.impl.CustomCategoryRepositoryImpl;
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
}