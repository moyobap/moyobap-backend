package com.moyobab.server.grouporder.entity;

import com.moyobab.server.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private boolean isClosed; // ture -> 모집종료, false -> 모집중

    private Long createdBy; // 유저 ID (주문이 어떤 유저에 의해서 생성되었는가를 표시)

    // 연관 관계는 이후에 추가
}
