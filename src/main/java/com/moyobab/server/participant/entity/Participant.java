package com.moyobab.server.participant.entity;

import com.moyobab.server.global.entity.BaseEntity;
import com.moyobab.server.user.entity.User;
import jakarta.persistence.*;
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

    private Long groupOrderId; // 참여한 그룹 주문 ID

    @Column(nullable = false)
    private Long orderAmount; // 해당 유저가 주문한 금액(원)

    @Builder.Default
    @Column(nullable = false)
    private boolean paid = false; // 결제 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
