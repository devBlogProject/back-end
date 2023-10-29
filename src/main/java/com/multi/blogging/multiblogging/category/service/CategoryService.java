package com.multi.blogging.multiblogging.category.service;

import com.multi.blogging.multiblogging.auth.exception.MemberNotFoundException;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import com.multi.blogging.multiblogging.base.SecurityUtil;
import com.multi.blogging.multiblogging.category.domain.Category;
import com.multi.blogging.multiblogging.category.dto.request.CategoryRequestDto;
import com.multi.blogging.multiblogging.category.exception.CategoryDuplicateException;
import com.multi.blogging.multiblogging.category.exception.CategoryNotFoundException;
import com.multi.blogging.multiblogging.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Category addTopCategory(String title) {
        var member = memberRepository.findOneByEmail(SecurityUtil.getCurrentMemberEmail()).orElseThrow(MemberNotFoundException::new);
        if (isDuplicate(member.getCategories(),title)) {
            throw new CategoryDuplicateException();
        }

        Category category = new Category(title, member);
        category.changeMember(member);

        return categoryRepository.save(category);
    }

    @Transactional
    public Category addChildCategory(String title, Long parentCategoryId) {
        var member = memberRepository.findOneByEmail(SecurityUtil.getCurrentMemberEmail()).orElseThrow(MemberNotFoundException::new);
        Category parentCategory = categoryRepository.findById(parentCategoryId).orElseThrow(CategoryNotFoundException::new);
        if (parentCategory.getMember()!=member){
            throw new CategoryNotFoundException();
        }
        if (isDuplicate(parentCategory.getChildrenCategories(),title)){
            throw new CategoryDuplicateException();
        }

        Category category = new Category(title, member);
        category.changeMember(member);
        category.changeParentCategory(parentCategory);

        return categoryRepository.save(category);
    }

    private boolean isDuplicate(List<Category> categories, String title){
        return categories.stream().filter(category -> category.getTitle().equals(title)).toList().size() != 0;
    }
}
