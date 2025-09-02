package com.moyobab.server.payment.exception;

import com.moyobab.server.global.exception.ErrorCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentErrorCase implements ErrorCase {

    PAYMENT_NOT_FOUND(404, 2001, "결제 정보를 찾을 수 없습니다."),
    PAYMENT_FAILED(400, 2002, "결제 처리에 실패했습니다.");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String message;
}
