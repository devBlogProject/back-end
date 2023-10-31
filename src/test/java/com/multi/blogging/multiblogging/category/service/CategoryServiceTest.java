package com.multi.blogging.multiblogging.category.service;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import com.multi.blogging.multiblogging.category.domain.Category;
import com.multi.blogging.multiblogging.category.dto.request.CategoryRequestDto;
import com.multi.blogging.multiblogging.category.exception.CategoryDuplicateException;
import com.multi.blogging.multiblogging.category.exception.CategoryNotFoundException;
import com.multi.blogging.multiblogging.category.repository.CategoryRepository;
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
    CategoryRepository categoryRepository;

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
        Category parent1=categoryService.addTopCategory("parent1");
        categoryService.addTopCategory("parent2");

        categoryService.addChildCategory(parent1.getId(),"child");
        assertThrows(CategoryDuplicateException.class, () ->
                categoryService.addTopCategory("parent1")
        );

        assertDoesNotThrow(() -> categoryService.addTopCategory("parent3"));
        assertDoesNotThrow(()->categoryService.addTopCategory("child"));

    }

    @Test
    @WithMockUser(username = testEmail)
    void 업데이트카테고리(){
        Member anotherMember = Member.builder().email("test2@test.com").password("anything").build();
        anotherMember=memberRepository.save(anotherMember);

        Category parent1=categoryService.addTopCategory("parent1");
        String oldTitle = parent1.getTitle();




        categoryService.updateCategory("parent2", parent1.getId());
        assertNotEquals(parent1.getTitle(),oldTitle);

        Category parent2 = new Category("parent2", anotherMember);
        Category savedParent2=categoryRepository.save(parent2);
        assertThrows(CategoryNotFoundException.class, () ->
                categoryService.updateCategory("parent3", savedParent2.getId()));
    }

    @Test
    @WithMockUser(username = testEmail)
    void 없는부모_카테고리에_추가() {
        assertThrows(CategoryNotFoundException.class, () ->
                categoryService.addChildCategory(1L, "child1")
        );
    }

    @Test
    @WithMockUser(username = testEmail)
    void 자식_카테고리_중복검사() {
        Category parentCategory=categoryService.addTopCategory("parent1");

        categoryService.addChildCategory(parentCategory.getId(),"child1" );
        categoryService.addChildCategory(parentCategory.getId(),"child2" );

        assertThrows(CategoryDuplicateException.class, () -> categoryService.addChildCategory( parentCategory.getId(),"child1"));
    }

    @Test
    @WithMockUser(username = testEmail)
    void 자식_자식_카테고리_중복검사() {
        Category parentCategory= categoryService.addTopCategory("parent1");
        Category childCategory=categoryService.addChildCategory( parentCategory.getId(),"child1");
        Category grandChildCategory=categoryService.addChildCategory(childCategory.getId(),"grand_child1" );

        assertThrows(CategoryDuplicateException.class,
                () -> categoryService.addChildCategory(childCategory.getId(),"grand_child1"));
    }
}