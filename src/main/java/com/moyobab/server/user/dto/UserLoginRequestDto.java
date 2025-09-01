package com.moyobab.server.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequestDto {
    private String email;
    private String password;
}
