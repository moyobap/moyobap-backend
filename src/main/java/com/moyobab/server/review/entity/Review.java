package com.moyobab.server.review.entity;

import com.moyobab.server.global.entity.BaseEntity;
import com.moyobab.server.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long groupOrderId;  // 어떤 그룹 주문에 대한 리뷰인지

    private Long userId;        // 리뷰 작성자

    private BigDecimal rating;   // 별점

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
