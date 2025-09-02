package com.moyobab.server.favorite.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteResponseDto {
    private Long id;
    private Long userId;
    private String category;
    private String brand;
    private int preferenceLevel;
}
