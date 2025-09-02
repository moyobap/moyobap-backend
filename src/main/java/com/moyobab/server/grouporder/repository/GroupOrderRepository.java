package com.moyobab.server.grouporder.repository;

import com.moyobab.server.grouporder.entity.GroupOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupOrderRepository extends JpaRepository<GroupOrder, Long> {
}
