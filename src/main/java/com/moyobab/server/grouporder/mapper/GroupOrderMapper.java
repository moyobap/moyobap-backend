package com.moyobab.server.grouporder.mapper;

import com.moyobab.server.grouporder.dto.GroupOrderResponseDto;
import com.moyobab.server.grouporder.entity.GroupOrder;

import java.time.format.DateTimeFormatter;

public class GroupOrderMapper {

    // 날짜,시간 포멧
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static GroupOrderResponseDto toResponse(GroupOrder groupOrder) {
        return GroupOrderResponseDto.builder()
                .id(groupOrder.getId())
                .menuCategory(groupOrder.getMenuCategory())
                .brandName(groupOrder.getBrandName())
                .expectedAmount(groupOrder.getExpectedAmount())
                .maxDistance(groupOrder.getMaxDistance())
                .deadlineTime(groupOrder.getDeadlineTime().format(formatter))
                .closed(groupOrder.isClosed())
                .currentOrderAmount(0)
                .creatorNickname(groupOrder.getCreator().getNickname())
                .build();
    }
}
