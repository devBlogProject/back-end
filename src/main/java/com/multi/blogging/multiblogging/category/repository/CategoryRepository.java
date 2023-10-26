package com.multi.blogging.multiblogging.category.repository;

import com.multi.blogging.multiblogging.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByTitle(String title);

    boolean existsByTitle(String title);
}
