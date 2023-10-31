package com.multi.blogging.multiblogging.category.service;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import com.multi.blogging.multiblogging.category.domain.Category;
import com.multi.blogging.multiblogging.category.exception.CategoryDuplicateException;
import com.multi.blogging.multiblogging.category.exception.CategoryNotFoundException;
import com.nimbusds.jose.proc.SecurityContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;

    @Autowired
    MemberRepository memberRepository;

    static final String testEmail = "test@test.com";


    @BeforeEach
    void setUp() {
        memberRepository.save(Member.builder().email(testEmail).password("1234").build());
    }

    @Test
    @WithMockUser(username = testEmail)
    void addTopCategory() {
        categoryService.addTopCategory("parent1");
        categoryService.addTopCategory("parent2");

        assertThrows(CategoryDuplicateException.class, () ->
                categoryService.addTopCategory("parent1")
        );

        assertDoesNotThrow(() -> categoryService.addTopCategory("parent3"));

    }

    @Test
    @WithMockUser(username = testEmail)
    void 없는부모_카테고리에_추가() {
        assertThrows(CategoryNotFoundException.class, () ->
                categoryService.addChildCategory("child1", 1L)
        );
    }

    @Test
    @WithMockUser(username = testEmail)
    void 자식_카테고리_중복검사() {
        Category parentCategory=categoryService.addTopCategory("parent1");

        categoryService.addChildCategory("child1", parentCategory.getId());
        categoryService.addChildCategory("child2", parentCategory.getId());

        assertThrows(CategoryDuplicateException.class, () -> categoryService.addChildCategory("child1", parentCategory.getId()));
    }

    @Test
    @WithMockUser(username = testEmail)
    void 자식_자식_카테고리_중복검사() {
        Category parentCategory= categoryService.addTopCategory("parent1");
        Category childCategory=categoryService.addChildCategory("child1", parentCategory.getId());
        Category grandChildCategory=categoryService.addChildCategory("grand_child1", childCategory.getId());

        assertThrows(CategoryDuplicateException.class,
                () -> categoryService.addChildCategory("grand_child1", childCategory.getId()));
    }
}