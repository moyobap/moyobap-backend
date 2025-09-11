package com.moyobab.server.user.repository;

import com.moyobab.server.user.entity.LoginType;
import com.moyobab.server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<User> findByEmailAndLoginType(String email, LoginType loginType);
}