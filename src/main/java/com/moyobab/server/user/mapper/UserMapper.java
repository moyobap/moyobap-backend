package com.moyobab.server.user.mapper;

import com.moyobab.server.user.dto.UserResponseDto;
import com.moyobab.server.user.entity.User;

public class UserMapper {

    public static UserResponseDto toUserResponse(User user) {
        return UserResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }
}
