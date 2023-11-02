package com.multi.blogging.multiblogging.category.controller;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import com.multi.blogging.multiblogging.category.domain.Category;
import com.multi.blogging.multiblogging.category.dto.request.CategoryRequestDto;
import com.multi.blogging.multiblogging.category.dto.response.CategoryResponseDto;
import com.multi.blogging.multiblogging.category.repository.CategoryRepository;
import com.multi.blogging.multiblogging.category.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static com.multi.blogging.multiblogging.category.domain.QCategory.category;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class CategoryControllerTest {


    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryService categoryService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MockMvc mockMvc;


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
    void getMyCategories() throws Exception {
        Category parent1=categoryService.addTopCategory("parent1");
        Category parent2=categoryService.addTopCategory("parent2");
        Category parent3=categoryService.addTopCategory("parent3");


        categoryService.addChildCategory(parent1.getId(),"child1" );
        categoryService.addChildCategory(parent1.getId(),"child2" );
        categoryService.addChildCategory(parent1.getId(),"child3" );
        categoryService.addChildCategory(parent1.getId(),"child4" );

        String uri = "/category/all";
        mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(3))
                .andExpect(jsonPath("$.data[0].childrenCategories.length()").value(4));

    }

    @Test
    @WithMockUser(username = testEmail)
    @Transactional
    void 업데이트카테고리() throws Exception {
        Category category = categoryService.addTopCategory("parent");
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setTitle("parent1");

        String uri = String.format("/category/%d", category.getId());
        mockMvc.perform(patch(uri)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("parent1"));
    }

    @Test
    @WithMockUser(username = testEmail)
    void 고아객체_제거_테스트() throws Exception {
        var parent=categoryService.addTopCategory("parent");
        categoryService.addChildCategory(parent.getId(), "child1");
        categoryService.addChildCategory(parent.getId(), "child2");
        categoryService.addChildCategory(parent.getId(), "child3");

        mockMvc.perform(delete("/category/{id}",parent.getId()))
                .andExpect(status().isOk())
                .andDo(print());

        String uri = "/category/all";
        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(0));



    }
}