package com.moyobab.server.menucategory.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuCategoryResponseDto {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
}
