package com.moyobab.server.user.service;

import com.moyobab.server.global.exception.ApplicationException;
import com.moyobab.server.user.dto.UserSignUpRequestDto;
import com.moyobab.server.user.entity.LoginType;
import com.moyobab.server.user.entity.User;
import com.moyobab.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.moyobab.server.user.exception.UserErrorCase;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(UserSignUpRequestDto req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new ApplicationException(UserErrorCase.EMAIL_DUPLICATED);
        }
        if (userRepository.existsByNickname(req.getNickname())) {
            throw new ApplicationException(UserErrorCase.NICKNAME_DUPLICATED);
        }

        User user = User.createUser(
                req.getEmail().toLowerCase(),
                passwordEncoder.encode(req.getPassword()),
                req.getUsername(),
                req.getNickname(),
                req.getBirthDate(),
                req.getPhoneNumber(),
                req.getLoginType() == null ? LoginType.BASIC : req.getLoginType()
        );

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("email")) {
                throw new ApplicationException(UserErrorCase.EMAIL_DUPLICATED);
            } else if (e.getMessage().contains("nickname")) {
                throw new ApplicationException(UserErrorCase.NICKNAME_DUPLICATED);
            }
            throw e;
        }
    }

    public boolean isNicknameAvailable(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }
}
