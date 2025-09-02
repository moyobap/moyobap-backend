package com.moyobab.server.review.entity;

import com.moyobab.server.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    // 연관관계는 추후 설정
}
