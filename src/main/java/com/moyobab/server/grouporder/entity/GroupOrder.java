package com.moyobab.server.grouporder.entity;

import com.moyobab.server.global.entity.BaseEntity;
import com.moyobab.server.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GroupOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String menuCategory; // 메뉴 카테고리 (예: 분식,피자 등등)

    private String brandName; // 브랜드 이름

    private int expectedAmount; // 그룹 주문을 위해 예상되는 최소주문금액

    private int maxDistance; // m or km로 설정

    private LocalDateTime deadlineTime; // 그룹 주문 마감 시간

    private boolean closed; // true -> 모집종료, false -> 모집중

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;
}
