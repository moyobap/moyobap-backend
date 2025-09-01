package com.moyobab.server.user.mapper;

import com.moyobab.server.user.dto.UserResponse;
import com.moyobab.server.user.entity.User;

public class UserMapper {

    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }
}
