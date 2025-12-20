package com.moyobab.server.event.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GroupOrderSummaryDto {
    private Long id;
    private String menuCategory;
    private String brandName;
    private int expectedAmount;
    private boolean closed;
    private LocalDateTime deadlineTime;
    private int participantCount;
    private long totalOrderAmount;
}
