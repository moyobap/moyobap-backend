package com.moyobab.server.user.exception;


import com.moyobab.server.global.exception.ErrorCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCase implements ErrorCase {

    USER_NOT_FOUND(404, 1001, "사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(409,1002,"이미 존재하는 회원입니다."),
    EMAIL_REQUIRED(400, 1003, "이메일은 필수입니다."),
    EMAIL_DUPLICATED(409, 1004, "이미 사용 중인 이메일입니다."),
    NICKNAME_REQUIRED(400, 1005, "닉네임은 필수입니다."),
    NICKNAME_DUPLICATED(409, 1006, "이미 사용 중인 닉네임입니다.");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String message;
}
