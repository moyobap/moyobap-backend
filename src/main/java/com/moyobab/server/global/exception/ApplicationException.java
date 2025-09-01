package com.moyobab.server.global.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private final ErrorCase errorCase;

    public ApplicationException(ErrorCase errorCase) {
        super(errorCase.getMessage());
        this.errorCase = errorCase;
    }
}
