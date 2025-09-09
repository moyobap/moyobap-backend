package com.moyobab.server.review.mapper;

import com.moyobab.server.review.dto.ReviewResponseDto;
import com.moyobab.server.review.entity.Review;

public class ReviewMapper {

    public static ReviewResponseDto toReviewResponse(Review review) {
        return ReviewResponseDto.builder()
                .id(review.getId())
                .groupOrderId(review.getGroupOrderId())
                .userId(review.getUser().getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .build();
    }
}
