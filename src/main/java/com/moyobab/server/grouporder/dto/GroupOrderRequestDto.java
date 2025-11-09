package com.moyobab.server.grouporder.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupOrderRequestDto {
    private String menuCategory;
    private String brandName;
    private int expectedAmount;
    private int maxDistance;
    private int durationMinutes; // 마감까지 걸리는 시간
}
