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

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
//    public Category addCategory(CategoryRequestDto categoryRequestDto){
//        Category category;
//        if (categoryRequestDto.getParentCategoryTitle()==null){
//           category= addTopCategory(categoryRequestDto.getTitle());
//        }else{
////            Category parentCategory = categoryRepository.find
//
//        }
//        return category;
//    }

    private Category addTopCategory(String title){
        var member = memberRepository.findOneByEmail(SecurityUtil.getCurrentMemberEmail()).orElseThrow(MemberNotFoundException::new);
        if (member.getCategories().contains(title)){
            throw new CategoryDuplicateException();
        }

        Category category = new Category(title,member);
        categoryRepository.save(category);

        return category;
    }
}
