package com.multi.blogging.multiblogging.category.repository.custom;


import com.multi.blogging.multiblogging.category.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CustomCategoryRepository {
    Optional<Category> findByIdWithMember(Long categoryId);

    List<Category> findTopCategoriesWithChildCategoriesByMemberNickname(String nickname);

    List<Category> findTopCategoriesWithBoardByMemberNickname(String nickname);
    Optional<Category> findByIdWithParentCategory(Long id);

    Optional<Category> findByIdWithChildCategories(Long id);

    Optional<Category> findByIdWithMemberAndBoard(Long id);

}
