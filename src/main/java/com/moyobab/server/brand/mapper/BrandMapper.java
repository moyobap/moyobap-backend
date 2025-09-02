package com.moyobab.server.brand.mapper;

import com.moyobab.server.brand.dto.BrandResponseDto;
import com.moyobab.server.brand.entity.Brand;

public class BrandMapper {

    public static BrandResponseDto toResponse(Brand brand) {
        return BrandResponseDto.builder()
                .id(brand.getId())
                .name(brand.getName())
                .category(brand.getCategory())
                .location(brand.getLocation())
                .imageUrl(brand.getImageUrl())
                .build();
    }
}
