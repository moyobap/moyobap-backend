package com.moyobab.server.grouporder.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupOrderResponseDto {
    private Long id;
    private String menuCategory;
    private String brandName;
    private int expectedAmount;
    private int maxDistance;
    private String deadlineTime;
    private boolean closed;
}
