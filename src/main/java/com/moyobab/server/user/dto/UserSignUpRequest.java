package com.moyobab.server.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequest {
    private String username;
    private String email;
    private String password;
    private String nickname;
    private String birthDate;
    private String phone;
}
