package com.moyobab.server.participant.entity;

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
// 그룹 참여자 관련 엔티티
public class Participant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // 참여한 유저 ID

    private Long groupOrderId; // 참여한 그룹 주문 ID

    private int orderAmount; // 해당 유저가 주문한 금액

    private boolean paid; // 결제 여부

    // 연관관계 매핑 이후 추가
}
