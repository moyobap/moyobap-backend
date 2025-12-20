package com.moyobab.server.grouporder.service;

import com.moyobab.server.event.dto.GroupOrderEventType;
import com.moyobab.server.event.service.GroupOrderEventPublisher;
import com.moyobab.server.grouporder.dto.GroupOrderRequestDto;
import com.moyobab.server.grouporder.dto.GroupOrderResponseDto;
import com.moyobab.server.grouporder.entity.GroupOrder;
import com.moyobab.server.grouporder.mapper.GroupOrderMapper;
import com.moyobab.server.grouporder.mapper.GroupOrderSummaryMapper;
import com.moyobab.server.grouporder.repository.GroupOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.moyobab.server.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupOrderService {

    private final GroupOrderRepository groupOrderRepository;
    private final GroupOrderEventPublisher eventPublisher;
    private final GroupOrderSummaryMapper summaryMapper;

    public List<GroupOrderResponseDto> getActiveGroupOrders() {
        return groupOrderRepository.findAllByClosedFalseOrderByDeadlineTimeAsc().stream()
                .map(GroupOrderMapper::toResponse)
                .collect(Collectors.toList());
    }

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
        var summary = summaryMapper.toSummary(saved, 0, 0L);
        eventPublisher.publishToGroupList(GroupOrderEventType.GROUP_CREATED, summary);
        log.info("[WS] GROUP_CREATED published: {}", summary);
        return GroupOrderMapper.toResponse(saved);
    }
}