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

import static com.multi.blogging.multiblogging.Constant.TEST_EMAIL;
import static com.multi.blogging.multiblogging.Constant.TEST_NICK;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    MemberRepository memberRepository;


    @BeforeEach
    void setUp() {
        memberRepository.save(Member.builder().email(TEST_EMAIL).password("1234").nickName(TEST_NICK).build());
    }

    @Test
    @Transactional
    void addTopCategory() {
        Category parent1 = categoryService.addTopCategory("parent1", TEST_EMAIL);
        categoryService.addTopCategory("parent2", TEST_EMAIL);

        categoryService.addChildCategory(parent1.getId(), "child", TEST_EMAIL);
        assertThrows(CategoryDuplicateException.class, () ->
                categoryService.addTopCategory("parent1", TEST_EMAIL)
        );

        assertDoesNotThrow(() -> categoryService.addTopCategory("parent3", TEST_EMAIL));
        assertDoesNotThrow(() -> categoryService.addTopCategory("child", TEST_EMAIL));

    }

    @Test
    @Transactional
    @WithMockUser(username = TEST_EMAIL)
    void 업데이트카테고리() {
        Category parent1 = categoryService.addTopCategory("parent1", TEST_EMAIL);
        String oldTitle = parent1.getTitle();

        categoryService.updateCategory("parent2", parent1.getId(), TEST_EMAIL);
        assertNotEquals(parent1.getTitle(), oldTitle);

//        Category parent2 = new Category("parent2", anotherMember);
//        Category savedParent2=categoryRepository.save(parent2);
//        assertThrows(CategoryNotFoundException.class, () ->
//                categoryService.updateCategory("parent3", savedParent2.getId()));
    }

    @Test
    @Transactional
    @WithMockUser(username = TEST_EMAIL)
    void 업데이트카테고리_중복_테스트() {
        Category parent1 = categoryService.addTopCategory("parent1", TEST_EMAIL);
        Category parent2 = categoryService.addTopCategory("parent2", TEST_EMAIL);
        Category parent3 = categoryService.addTopCategory("parent3", TEST_EMAIL);

        assertThrows(CategoryDuplicateException.class, () -> categoryService.updateCategory("parent2", parent1.getId(), TEST_EMAIL));

        Category child1 = categoryService.addChildCategory(parent1.getId(), "child1", TEST_EMAIL);
        Category child2 = categoryService.addChildCategory(parent1.getId(), "child2", TEST_EMAIL);
        Category child3 = categoryService.addChildCategory(parent1.getId(), "child3", TEST_EMAIL);

        assertThrows(CategoryDuplicateException.class, () -> categoryService.updateCategory("child2", child1.getId(), TEST_EMAIL));
    }

    @Test
    @Transactional
    @WithMockUser(username = TEST_EMAIL)
    void 없는부모_카테고리에_추가() {
        assertThrows(CategoryNotFoundException.class, () ->
                categoryService.addChildCategory(1L, "child1", TEST_EMAIL)
        );
    }

    @Test
    @Transactional
    @WithMockUser(username = TEST_EMAIL)
    void 자식_카테고리_중복검사() {
        Category parentCategory = categoryService.addTopCategory("parent1", TEST_EMAIL);

        categoryService.addChildCategory(parentCategory.getId(), "child1", TEST_EMAIL);
        categoryService.addChildCategory(parentCategory.getId(), "child2", TEST_EMAIL);

        assertThrows(CategoryDuplicateException.class, () -> categoryService.addChildCategory(parentCategory.getId(), "child1", TEST_EMAIL));
    }

    @Test
    @Transactional
    @WithMockUser(username = TEST_EMAIL)
    void 자식_자식_카테고리_중복검사() {
        Category parentCategory = categoryService.addTopCategory("parent1", TEST_EMAIL);
        Category childCategory = categoryService.addChildCategory(parentCategory.getId(), "child1", TEST_EMAIL);
        Category grandChildCategory = categoryService.addChildCategory(childCategory.getId(), "grand_child1", TEST_EMAIL);

        assertThrows(CategoryDuplicateException.class,
                () -> categoryService.addChildCategory(childCategory.getId(), "grand_child1", TEST_EMAIL));
    }
}