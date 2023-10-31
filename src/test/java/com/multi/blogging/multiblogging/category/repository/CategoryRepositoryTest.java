package com.multi.blogging.multiblogging.category.repository;

import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import com.multi.blogging.multiblogging.category.domain.Category;
import com.multi.blogging.multiblogging.config.QueryDslTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Import(QueryDslTestConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    MemberRepository memberRepository;

    private final static String testEmail = "test@test.com";
    @BeforeEach
    void setUp(){
        memberRepository.save(Member.builder()
                .email(testEmail).password("1234").build());
    }

    @Test
    void findByTitle() {
        var member = memberRepository.findOneByEmail(testEmail).orElseThrow();
        Category category = new Category("test", member);
        Category category1 = new Category("test1", member);

        categoryRepository.save(category);
        categoryRepository.save(category1);


        assertTrue(categoryRepository.findByTitle("test").isPresent());
        assertTrue(categoryRepository.findByTitle("test1").isPresent());
        assertFalse(categoryRepository.findByTitle("test2").isPresent());

    }


    @Test
    void findByIdWithMember(){
        var existedMember = memberRepository.findOneByEmail(testEmail).orElseThrow();

        Member newMember = Member.builder().email("new@new.com").password("1234").build();
        newMember = memberRepository.save(newMember);

        Category category1 = new Category("test", existedMember);
        Category category2 = new Category("test1", newMember);

        category1=categoryRepository.save(category1);
        category2=categoryRepository.save(category2);


        assertTrue(categoryRepository.findByIdWithMember(existedMember,category1.getId()).isPresent());
        assertTrue(categoryRepository.findByIdWithMember(newMember,category2.getId()).isPresent());

        assertFalse(categoryRepository.findByIdWithMember(existedMember,category2.getId()).isPresent());
        assertFalse(categoryRepository.findByIdWithMember(newMember,category1.getId()).isPresent());
    }

    @Test
    void findAllTopCategoriesWithMember(){
        var member1 = memberRepository.findOneByEmail(testEmail).orElseThrow();
        Category category1 = new Category("test1", member1);
        Category category2 = new Category("test2", member1);
        Category category3 = new Category("test3", member1);

       Category parent1= categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);

        Category childCategory = new Category("child", member1);
        childCategory.changeParentCategory(parent1);
        categoryRepository.save(childCategory);

        Member member2 = Member.builder()
                .email("test2@test2.com")
                .password("1234")
                .nickName("test2")
                .build();

        member2=memberRepository.save(member2);

        Category category4 = new Category("test4", member2);
        categoryRepository.save(category4);

        assertEquals(categoryRepository.findAllTopCategoriesWithMember(member1).size(),3);
        assertEquals(categoryRepository.findAllTopCategoriesWithMember(member2).size(),1);
    }

}