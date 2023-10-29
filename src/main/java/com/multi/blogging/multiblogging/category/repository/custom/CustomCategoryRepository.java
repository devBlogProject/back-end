package com.multi.blogging.multiblogging.category.repository.custom;


import com.multi.blogging.multiblogging.auth.domain.Member;
import com.multi.blogging.multiblogging.category.domain.Category;

import java.util.Optional;

public interface CustomCategoryRepository {
    Optional<Category> findByIdWithMember(Member member,Long categoryId);
}
