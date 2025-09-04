package com.moyobab.server.favorite.exception;

import com.moyobab.server.global.exception.ErrorCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FavoriteErrorCase implements ErrorCase {

    FAVORITE_NOT_FOUND(404, 3001, "해당 선호 항목을 찾을 수 없습니다.");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String message;
}
