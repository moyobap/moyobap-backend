package com.moyobab.server.grouporder.mapper;

import com.moyobab.server.event.dto.GroupOrderSummaryDto;
import com.moyobab.server.grouporder.entity.GroupOrder;
import org.springframework.stereotype.Component;

@Component
public class GroupOrderSummaryMapper {

    public GroupOrderSummaryDto toSummary(
            GroupOrder groupOrder,
            int participantCount,
            long totalAmount
    ) {
        return GroupOrderSummaryDto.builder()
                .id(groupOrder.getId())
                .menuCategory(groupOrder.getMenuCategory())
                .brandName(groupOrder.getBrandName())
                .expectedAmount(groupOrder.getExpectedAmount())
                .closed(groupOrder.isClosed())
                .deadlineTime(groupOrder.getDeadlineTime())
                .participantCount(participantCount)
                .totalOrderAmount(totalAmount)
                .build();
    }
}