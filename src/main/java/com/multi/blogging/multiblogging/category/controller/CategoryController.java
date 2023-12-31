package com.multi.blogging.multiblogging.category.controller;

import com.multi.blogging.multiblogging.base.ApiResponse;
import com.multi.blogging.multiblogging.category.domain.Category;
import com.multi.blogging.multiblogging.category.dto.request.CategoryRequestDto;
import com.multi.blogging.multiblogging.category.dto.response.CategoryResponseDto;
import com.multi.blogging.multiblogging.category.exception.CategoryAccessPermissionDeniedException;
import com.multi.blogging.multiblogging.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @io.swagger.v3.oas.annotations.responses.ApiResponse(description = "상위 카테고리 작성")
    public ApiResponse<CategoryResponseDto> writeTopCategory(@Valid @RequestBody CategoryRequestDto requestDto, Authentication authentication) {
        Category category = categoryService.addTopCategory(requestDto.getTitle(), authentication.getName());
        return ApiResponse.createSuccess(CategoryResponseDto.of(category));
    }

    @PostMapping("/{parent_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CategoryResponseDto> writeChildCategory(@Valid @RequestBody CategoryRequestDto requestDto, @PathVariable("parent_id") Long parentCategoryId, Authentication authentication) {
        Category category = categoryService.addChildCategory(parentCategoryId, requestDto.getTitle(), authentication.getName());
        return ApiResponse.createSuccess(CategoryResponseDto.of(category));
    }

    @GetMapping("/{nickname}/all")
    public ApiResponse<List<CategoryResponseDto>> getTopCategories(@PathVariable String nickname) {
        List<Category> categories = categoryService.getTopCategories(nickname);
        return ApiResponse.createSuccess(categories.stream().map(CategoryResponseDto::of).toList());
    }

    @PatchMapping("/{category_id}")
    public ApiResponse<CategoryResponseDto> updateCategory(@Valid @RequestBody CategoryRequestDto requestDto, @PathVariable("category_id") Long categoryId,Authentication authentication) {
        Category updatedCategory = categoryService.updateCategory(requestDto.getTitle(), categoryId,authentication.getName());
        return ApiResponse.createSuccess(CategoryResponseDto.of(updatedCategory));
    }

    @DeleteMapping("/{category_id}")
    public ApiResponse<?> deleteCategory(@PathVariable("category_id") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ApiResponse.createSuccessWithNoContent();
    }
}
