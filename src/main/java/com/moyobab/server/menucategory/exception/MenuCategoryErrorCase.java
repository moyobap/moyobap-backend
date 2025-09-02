package com.moyobab.server.menucategory.exception;

import com.moyobab.server.global.exception.ErrorCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuCategoryErrorCase implements ErrorCase {

    CATEGORY_NOT_FOUND(404, 6001, "해당 카테고리를 찾을 수 없습니다.");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String message;
}
