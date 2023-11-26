package com.multi.blogging.multiblogging.category.service;

import com.multi.blogging.multiblogging.auth.exception.MemberNotFoundException;
import com.multi.blogging.multiblogging.auth.repository.MemberRepository;
import com.multi.blogging.multiblogging.category.domain.Category;
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
    public Category addTopCategory(String title,String memberEmail) {
        var member = memberRepository.findOneByEmailWithCategories(memberEmail).orElseThrow(MemberNotFoundException::new);
        List<Category> topCategories= member.getCategories().stream().filter(category -> category.getParent()==null).toList();
        if (isDuplicate(topCategories,title)) {
            throw new CategoryDuplicateException();
        }

        Category category = new Category(title, member);
        category.changeMember(member);

        return categoryRepository.save(category);
    }

    @Transactional
    public Category addChildCategory(Long parentCategoryId,String title,String memberEmail) {
        var member = memberRepository.findOneByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
        Category parentCategory = categoryRepository.findByIdWithChildCategories(parentCategoryId).orElseThrow(CategoryNotFoundException::new);
        if (isDuplicate(parentCategory.getChildrenCategories(),title)){
            throw new CategoryDuplicateException();
        }

        Category category = new Category(title, member);
        category.changeMember(member);
        category.changeParentCategory(parentCategory);

        return categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public List<Category> getTopCategories(String nickname){
        List<Category> categories1 = categoryRepository.findTopCategoriesWithChildCategoriesByMemberNickname(nickname);
        List<Category> categories2 = categoryRepository.findTopCategoriesWithBoardByMemberNickname(nickname);
        for (int i=0;i<categories1.size();i++){
            categories1.get(i).setBoards(categories2.get(i).getBoards());
        }
        return categories1;
    }

    @Transactional
    public Category updateCategory(String title,Long categoryId,String memberEmail){
        var category = categoryRepository.findByIdWithParentCategory(categoryId).orElseThrow(CategoryNotFoundException::new);
        List<Category> siblingCategories;
        if (category.getParent()==null){
            var member = memberRepository.findOneByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
            siblingCategories=categoryRepository.findTopCategoriesWithChildCategoriesByMemberNickname(member.getNickName());
        }else{
            siblingCategories = category.getParent().getChildrenCategories();
        }
        if (isDuplicate(siblingCategories,title)){
            throw new CategoryDuplicateException();
        }

        category.setTitle(title);

        return category;
    }

    @Transactional
    public void deleteCategory(Long id){
        categoryRepository.deleteById(id);
    }

    private boolean isDuplicate(List<Category> categories, String title){
        return categories.stream().filter(category -> category.getTitle().equals(title)).toList().size() != 0;
    }
}
