package com.moyobab.server.brand.exception;

import com.moyobab.server.global.exception.ErrorCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BrandErrorCase implements ErrorCase {

    BRAND_NOT_FOUND(404, 5001, "해당 브랜드를 찾을 수 없습니다.");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String message;
}
