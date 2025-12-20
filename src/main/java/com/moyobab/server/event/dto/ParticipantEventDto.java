package com.moyobab.server.event.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParticipantEventDto {
    private Long participantId;
    private Long userId;
    private String nickname;
    private long orderAmount;
    private boolean paid;
}
