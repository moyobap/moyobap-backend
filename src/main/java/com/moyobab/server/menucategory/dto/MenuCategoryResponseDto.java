package com.moyobab.server.menucategory.dto;

import com.moyobab.server.menucategory.entity.MenuCategoryType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuCategoryResponseDto {
    private String name;
    private String displayName;
    private String kakaoCategoryKeyword;

    public static MenuCategoryResponseDto from(MenuCategoryType type) {
        return MenuCategoryResponseDto.builder()
                .name(type.name())
                .displayName(type.getDisplayName())
                .kakaoCategoryKeyword(type.getKakaoCategoryKeyword())
                .build();
    }
}