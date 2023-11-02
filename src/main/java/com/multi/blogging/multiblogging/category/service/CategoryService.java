package com.multi.blogging.multiblogging.category.service;

import com.multi.blogging.multiblogging.auth.exception.MemberNotFoundException;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import com.multi.blogging.multiblogging.base.SecurityUtil;
import com.multi.blogging.multiblogging.category.domain.Category;
import com.multi.blogging.multiblogging.category.exception.CategoryAccessPermissionDeniedException;
import com.multi.blogging.multiblogging.category.exception.CategoryDuplicateException;
import com.multi.blogging.multiblogging.category.exception.CategoryNotFoundException;
import com.multi.blogging.multiblogging.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Category addTopCategory(String title) {
        var member = memberRepository.findOneByEmail(SecurityUtil.getCurrentMemberEmail()).orElseThrow(MemberNotFoundException::new);
        List<Category> topCategories= member.getCategories().stream().filter(category -> category.getParent()==null).toList();
        if (isDuplicate(topCategories,title)) {
            throw new CategoryDuplicateException();
        }

        Category category = new Category(title, member);
        category.changeMember(member);

        return categoryRepository.save(category);
    }

    @Transactional
    public Category addChildCategory(Long parentCategoryId,String title) {
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

    @Transactional(readOnly = true)
    public List<Category> getMyTopCategories(){
        var member = memberRepository.findOneByEmail(SecurityUtil.getCurrentMemberEmail()).orElseThrow(MemberNotFoundException::new);
        return categoryRepository.findAllTopCategoriesWithMember(member);
    }

    @Transactional
    public Category updateCategory(String title,Long categoryId){
        var category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        if (!category.getMember().getEmail().equals(SecurityUtil.getCurrentMemberEmail())){
            throw new CategoryNotFoundException();
        }

        category.setTitle(title);
        return category;
    }

    @Transactional
    public void deleteCategory(Long id){
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()){
            if (!category.get().getMember().getEmail().equals(SecurityUtil.getCurrentMemberEmail())){
                throw new CategoryAccessPermissionDeniedException();
            }
        }

        categoryRepository.deleteById(id);
    }

    private boolean isDuplicate(List<Category> categories, String title){
        return categories.stream().filter(category -> category.getTitle().equals(title)).toList().size() != 0;
    }
}
