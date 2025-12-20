package com.moyobab.server.participant.exception;

import com.moyobab.server.global.exception.ErrorCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ParticipantErrorCase implements ErrorCase {

    GROUP_ORDER_NOT_FOUND(404, 4000, "그룹 주문을 찾을 수 없습니다."),
    PARTICIPANT_NOT_FOUND(404, 4001, "참여자를 찾을 수 없습니다."),
    USER_NOT_FOUND(404, 4002, "유저를 찾을 수 없습니다."),
    GROUP_ORDER_CLOSED(400, 4100, "이미 모집이 종료된 그룹입니다."),
    ALREADY_JOINED(400, 4101, "이미 참여한 그룹입니다."),
    INVALID_ORDER_AMOUNT(400, 4102, "주문 금액이 올바르지 않습니다."),
    LOGIN_REQUIRED(401, 4200, "로그인이 필요합니다.");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String message;
}
