package com.moyobab.server.review.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDto {

    private Long id;
    private Long groupOrderId;
    private Long userId;
    private BigDecimal rating;
    private String comment;
}
