package com.multi.blogging.multiblogging.category.repository;

import com.multi.blogging.multiblogging.category.domain.Category;
import com.multi.blogging.multiblogging.category.repository.custom.CustomCategoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, CustomCategoryRepository {

    Optional<Category> findByTitle(String title);

}
