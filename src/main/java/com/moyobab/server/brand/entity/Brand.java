package com.moyobab.server.brand.entity;

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
public class Brand extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 브랜드 이름 (예: 피자헛,교촌치킨,죠스떡볶이 등등)

    private String category; // 메뉴 카테고리 (예: 피자, 치킨 등)

    private String location; // 위치 정보

    private String imageUrl; // 브랜드 이미지 URL (필수 X)

    // 연관 관계는 이후 설정
}
