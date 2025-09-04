package com.moyobab.server.payment.entity;

import com.moyobab.server.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String paymentKey; // 결제 시스템에서 받는 결제 키

    @Column(nullable = false)
    private String orderId; // 내부 주문 식별자 (예: 참여자 기반으로 생성)

    @Column(nullable = false)
    private Long participantId; // 결제한 참여자의 ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status; // 결제 상태

    @Column(nullable = false)
    private Long amount; // 결제 금액

    @Column(nullable = true)
    private String paymentMethod; // 결제 수단 (예: 카드, 카카오페이 등)

    private LocalDateTime approvedAt; // 승인 시간

    private String failReason; // 실패 사유 (필요 X)

    // 연관관계 추후 설정
}
