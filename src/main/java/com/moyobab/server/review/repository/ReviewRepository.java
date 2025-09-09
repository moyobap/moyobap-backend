package com.moyobab.server.review.repository;

import com.moyobab.server.review.entity.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @EntityGraph(attributePaths = {"user"})
    List<Review> findByUser_Id(Long userId);

    @EntityGraph(attributePaths = {"user"})
    List<Review> findByGroupOrderId(Long groupOrderId);
}
