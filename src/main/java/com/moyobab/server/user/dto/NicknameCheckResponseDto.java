package com.moyobab.server.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NicknameCheckResponseDto {
    private boolean available;
    private String message;
}
