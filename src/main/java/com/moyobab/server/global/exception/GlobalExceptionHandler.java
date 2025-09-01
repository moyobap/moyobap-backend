package com.moyobab.server.global.exception;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyobab.server.global.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<CommonResponse<?>> handleApplicationException(ApplicationException e) {
        CommonResponse<?> response = CommonResponse.error(e.getErrorCase());
        return ResponseEntity
                .status(e.getErrorCase().getHttpStatusCode())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<?>> handleValidException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        CommonResponse<?> response = CommonResponse.error(400, message);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}
