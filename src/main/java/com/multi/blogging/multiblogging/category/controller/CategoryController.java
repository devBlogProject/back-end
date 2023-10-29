package com.multi.blogging.multiblogging.category.controller;

import com.multi.blogging.multiblogging.base.ApiResponse;
import com.multi.blogging.multiblogging.category.domain.Category;
import com.multi.blogging.multiblogging.category.dto.request.CategoryRequestDto;
import com.multi.blogging.multiblogging.category.dto.response.CategoryResponseDto;
import com.multi.blogging.multiblogging.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(description = "상위 카테고리 작성")
    public ApiResponse<CategoryResponseDto> writeTopCategory(@Valid @RequestBody CategoryRequestDto requestDto){
        Category category = categoryService.addTopCategory(requestDto.getTitle());
        return ApiResponse.createSuccess(CategoryResponseDto.of(category));
    }

    @PostMapping("/{parent_id}/")
    public ApiResponse<CategoryResponseDto> writeChildCategory(@Valid @RequestBody CategoryRequestDto requestDto, @PathVariable("parent_id")Long parentCategoryId ){
        Category category = categoryService.addChildCategory(requestDto.getTitle(),parentCategoryId);
        return ApiResponse.createSuccess(CategoryResponseDto.of(category));
    }

    @GetMapping("/all")
    public ApiResponse<List<CategoryResponseDto>> getMyCategories(){
        List<Category> categories = categoryService.getMyTopCategories();
        return ApiResponse.createSuccess(categories.stream().map(CategoryResponseDto::of).toList());
    }

}
