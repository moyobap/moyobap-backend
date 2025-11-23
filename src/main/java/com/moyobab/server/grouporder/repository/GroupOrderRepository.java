package com.moyobab.server.grouporder.repository;

import com.moyobab.server.grouporder.entity.GroupOrder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupOrderRepository extends JpaRepository<GroupOrder, Long> {
    @EntityGraph(attributePaths = {"participants", "creator"})
    List<GroupOrder> findAllByClosedFalseOrderByDeadlineTimeAsc();  // 모집 중 + 마감 임박순 정렬
}
