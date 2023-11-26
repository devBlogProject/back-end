package com.multi.blogging.multiblogging.category.dto.response;

import com.multi.blogging.multiblogging.base.BaseResponseDto;
import com.multi.blogging.multiblogging.category.domain.Category;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CategoryResponseDto extends BaseResponseDto {

    private Long id;

    private String title;

    private List<BoardResponseDto> boards;

    private List<CategoryResponseDto> childrenCategories;



    @Builder
    private CategoryResponseDto(Long id, String title, List<CategoryResponseDto> childrenCategories, List<BoardResponseDto> boardResponseDtoList,LocalDateTime createdDate,LocalDateTime updatedDate){
        this.id = id;
        this.title = title;
        this.childrenCategories = childrenCategories;
        this.boards =boardResponseDtoList;
        this.createdDate=createdDate;
        this.updatedDate = updatedDate;
    }
    public static CategoryResponseDto of(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .title(category.getTitle())
                .childrenCategories(category.getChildrenCategories().stream().map(CategoryResponseDto::of).toList())
                .boardResponseDtoList(category.getBoards().stream().map(BoardResponseDto::of).toList())
                .createdDate(category.getCreatedDate())
                .updatedDate(category.getUpdatedDate())
                .build();
    }
}
