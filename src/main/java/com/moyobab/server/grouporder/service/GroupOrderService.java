package com.moyobab.server.grouporder.service;

import com.moyobab.server.grouporder.dto.GroupOrderRequestDto;
import com.moyobab.server.grouporder.dto.GroupOrderResponseDto;
import com.moyobab.server.grouporder.entity.GroupOrder;
import com.moyobab.server.grouporder.mapper.GroupOrderMapper;
import com.moyobab.server.grouporder.repository.GroupOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.moyobab.server.user.entity.User;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GroupOrderService {

    private final GroupOrderRepository groupOrderRepository;

    public GroupOrderResponseDto createGroupOrder(GroupOrderRequestDto request, User creator) {
        LocalDateTime deadline = LocalDateTime.now().plusMinutes(request.getDurationMinutes());

        GroupOrder groupOrder = GroupOrder.builder()
                .menuCategory(request.getMenuCategory())
                .brandName(request.getBrandName())
                .expectedAmount(request.getExpectedAmount())
                .maxDistance(request.getMaxDistance())
                .deadlineTime(deadline)
                .closed(false)
                .creator(creator)
                .build();

        GroupOrder saved = groupOrderRepository.save(groupOrder);
        return GroupOrderMapper.toResponse(saved);
    }
}