package com.moyobab.server.favorite.entity;

import com.moyobab.server.global.entity.BaseEntity;
import com.moyobab.server.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;          // 선호도를 등록한 사용자 ID

    private String category;      // 메뉴 카테고리 (예: 피자, 치킨 등)

    private String brand;         // 브랜드 이름 (예: 피자헛, 교촌치킨)

    private int preferenceLevel;  // 선호도 수준 (예: 1~5)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
