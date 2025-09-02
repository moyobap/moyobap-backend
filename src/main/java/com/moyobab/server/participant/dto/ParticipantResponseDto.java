package com.moyobab.server.participant.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipantResponseDto {
    private Long id;
    private Long userId;
    private Long groupOrderId;
    private Long orderAmount;
    private boolean paid;
}
