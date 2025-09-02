package com.moyobab.server.menucategory.mapper;

import com.moyobab.server.menucategory.dto.MenuCategoryResponseDto;
import com.moyobab.server.menucategory.entity.MenuCategory;

public class MenuCategoryMapper {

    public static MenuCategoryResponseDto toResponse(MenuCategory category) {
        return MenuCategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .build();
    }
}
