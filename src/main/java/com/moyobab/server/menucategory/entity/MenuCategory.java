package com.moyobab.server.menucategory.entity;

import com.moyobab.server.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MenuCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 카테고리명 (예: 분식, 피자, 치킨 등)

    private String description; // 설명 (필수 X)

    private String imageUrl; // 카테고리 대표 이미지 (필수 X)

    // 연관 관계는 이후 설정
}
