package com.moyobab.server.user.service;

import com.moyobab.server.global.exception.ApplicationException;
import com.moyobab.server.user.dto.UserSignUpRequestDto;
import com.moyobab.server.user.entity.LoginType;
import com.moyobab.server.user.entity.User;
import com.moyobab.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.moyobab.server.user.exception.UserErrorCase;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(UserSignUpRequestDto req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new ApplicationException(UserErrorCase.USER_ALREADY_EXISTS);
        }

        User user = User.createUser(
                req.getEmail(),
                passwordEncoder.encode(req.getPassword()),
                req.getUsername(),
                req.getNickname(),
                req.getBirthDate(),
                req.getPhoneNumber(),
                req.getLoginType() == null ? LoginType.BASIC : req.getLoginType()
        );

        userRepository.save(user);
    }

    public boolean isNicknameAvailable(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }
}
