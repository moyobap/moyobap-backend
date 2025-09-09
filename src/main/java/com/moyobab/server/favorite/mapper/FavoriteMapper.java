package com.moyobab.server.favorite.mapper;

import com.moyobab.server.favorite.dto.FavoriteResponseDto;
import com.moyobab.server.favorite.entity.Favorite;

public class FavoriteMapper {

    public static FavoriteResponseDto toResponse(Favorite favorite) {
        return FavoriteResponseDto.builder()
                .id(favorite.getId())
                .userId(favorite.getUser().getId())
                .category(favorite.getCategory())
                .brand(favorite.getBrand())
                .preferenceLevel(favorite.getPreferenceLevel())
                .build();
    }
}
