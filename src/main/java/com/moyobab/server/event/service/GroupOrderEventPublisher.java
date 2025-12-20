package com.moyobab.server.event.service;

import com.moyobab.server.event.dto.GroupOrderEvent;
import com.moyobab.server.event.dto.GroupOrderEventType;
import com.moyobab.server.event.dto.GroupOrderSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupOrderEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public void publishToGroupList(GroupOrderEventType type, GroupOrderSummaryDto summary) {
        messagingTemplate.convertAndSend(
                "/topic/group-orders",
                GroupOrderEvent.<GroupOrderSummaryDto>builder()
                        .type(type)
                        .groupOrderId(summary.getId())
                        .payload(summary)
                        .build()
        );
    }

    public <T> void publishToGroupDetail(Long groupOrderId, GroupOrderEventType type, T payload) {
        messagingTemplate.convertAndSend(
                "/topic/group-orders/" + groupOrderId,
                GroupOrderEvent.<T>builder()
                        .type(type)
                        .groupOrderId(groupOrderId)
                        .payload(payload)
                        .build()
        );
    }
}
