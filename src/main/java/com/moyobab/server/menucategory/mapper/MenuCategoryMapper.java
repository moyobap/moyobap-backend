/* 지금은 필요 없는 파일 (혹시 나중에 DB 기반 메뉴 카테고리 조회를 써야한다면 그때 사용)
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
 */