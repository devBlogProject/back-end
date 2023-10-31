package com.multi.blogging.multiblogging.category.controller;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import com.multi.blogging.multiblogging.category.domain.Category;
import com.multi.blogging.multiblogging.category.dto.response.CategoryResponseDto;
import com.multi.blogging.multiblogging.category.repository.CategoryRepository;
import com.multi.blogging.multiblogging.category.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ActiveProfiles("test")
@SpringBootTest
class CategoryControllerTest {


    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryService categoryService;

    @Autowired
    MemberRepository memberRepository;

    static Member member;
    static final String testEmail = "test@test.com";

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        member=memberRepository.save(Member.builder().email(testEmail).password("1234").build());
    }
    @Test
    @WithMockUser(username = testEmail)
    @Transactional
    void getMyCategories(){
        Category parent1=categoryService.addTopCategory("parent1");
        Category parent2=categoryService.addTopCategory("parent2");
        Category parent3=categoryService.addTopCategory("parent3");


        categoryService.addChildCategory(parent1.getId(),"child1" );
        categoryService.addChildCategory(parent1.getId(),"child2" );
        categoryService.addChildCategory(parent1.getId(),"child3" );

        List<Category> categories = categoryService.getMyTopCategories();

        assertDoesNotThrow(()->System.out.println(
                objectMapper.writeValueAsString(
                        categories.stream().map(CategoryResponseDto::of).toList()
                )
                ));

    }

}