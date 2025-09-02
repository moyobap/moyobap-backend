package com.moyobab.server.brand.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandResponseDto {
    private Long id;
    private String name;
    private String category;
    private String location;
    private String imageUrl;
}
