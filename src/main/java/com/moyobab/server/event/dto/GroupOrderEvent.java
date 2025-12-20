package com.moyobab.server.event.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupOrderEvent<T> {
    private GroupOrderEventType type;
    private Long groupOrderId;
    private T payload;
}