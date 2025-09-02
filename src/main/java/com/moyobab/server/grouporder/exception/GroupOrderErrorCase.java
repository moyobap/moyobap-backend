package com.moyobab.server.grouporder.exception;

import com.moyobab.server.global.exception.ErrorCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GroupOrderErrorCase implements ErrorCase {

    GROUP_ORDER_NOT_FOUND(404, 3001, "그룹 주문 정보를 찾을 수 없습니다.");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String message;
}
