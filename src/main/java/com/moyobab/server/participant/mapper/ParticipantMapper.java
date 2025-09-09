package com.moyobab.server.participant.mapper;

import com.moyobab.server.participant.dto.ParticipantResponseDto;
import com.moyobab.server.participant.entity.Participant;

public class ParticipantMapper {

    public static ParticipantResponseDto toResponse(Participant participant) {
        return ParticipantResponseDto.builder()
                .id(participant.getId())
                .groupOrderId(participant.getGroupOrderId())
                .orderAmount(participant.getOrderAmount())
                .paid(participant.isPaid())
                .build();
    }
}
