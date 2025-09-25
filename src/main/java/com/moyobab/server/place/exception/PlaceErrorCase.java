package com.moyobab.server.place.exception;

import com.moyobab.server.global.exception.ErrorCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlaceErrorCase implements ErrorCase {

    CATEGORY_REQUIRED(400, 9001, "카테고리는 필수입니다."),
    LOCATION_REQUIRED(400, 9002, "좌표(x, y)는 필수입니다."),
    RADIUS_INVALID(400, 9003, "검색 반경은 0 ~ 10000 사이여야 합니다."),
    PLACE_NOT_FOUND(404, 9004, "해당 조건에 해당하는 장소를 찾을 수 없습니다.");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String message;
}
