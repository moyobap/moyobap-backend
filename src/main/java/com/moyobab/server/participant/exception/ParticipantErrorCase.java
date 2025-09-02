package com.moyobab.server.participant.exception;

import com.moyobab.server.global.exception.ErrorCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ParticipantErrorCase implements ErrorCase {

    PARTICIPANT_NOT_FOUND(404, 4001, "참여자를 찾을 수 없습니다.");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String message;
}
