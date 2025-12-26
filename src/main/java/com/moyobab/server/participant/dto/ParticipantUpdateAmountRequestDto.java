package com.moyobab.server.participant.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ParticipantUpdateAmountRequestDto {

    @Min(1)
    private long orderAmount;
}
