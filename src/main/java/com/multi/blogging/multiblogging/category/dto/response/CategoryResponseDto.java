package com.multi.blogging.multiblogging.category.dto.response;

import com.multi.blogging.multiblogging.board.domain.Board;
import com.multi.blogging.multiblogging.category.domain.Category;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryResponseDto {

    private Long id;

    private String title;

//    private List<Board> boards = new ArrayList<>();

    private List<CategoryResponseDto> childrenCategories;


    @Builder
    private CategoryResponseDto(Long id, String title,List<CategoryResponseDto> childrenCategories){
        this.id = id;
        this.title = title;
        this.childrenCategories = childrenCategories;
    }
    public static CategoryResponseDto of(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .title(category.getTitle())
                .childrenCategories(category.getChildrenCategories().stream().map(CategoryResponseDto::of).toList())
                .build();
    }
}
