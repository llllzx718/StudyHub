package com.studyhub.backend.service;

import com.studyhub.backend.dto.LoginRequest;
import com.studyhub.backend.dto.RegisterRequest;
import com.studyhub.backend.dto.UserResponse;
import com.studyhub.backend.model.User;
import com.studyhub.backend.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(RegisterRequest request) {

        String username = request.username();
        String password = request.password();

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("用户名不能为空");
        }

        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException(
                    "密码不能少于8个字符"
            );
        }

        if (userRepository
                .findByUsername(username)
                .isPresent()) {

            throw new IllegalArgumentException(
                    "用户名已存在"
            );
        }

        String passwordHash =
                passwordEncoder.encode(password);

        User user = userRepository.insert(
                username,
                passwordHash
        );

        return new UserResponse(
                user.id(),
                user.username()
        );
    }
    //登录
    public UserResponse login(LoginRequest request) {

        User user = userRepository
                .findByUsername(request.username())
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "用户名或密码错误"
                        )
                );

        if (user.passwordHash() == null ||
                !passwordEncoder.matches(
                        request.password(),
                        user.passwordHash()
                )) {

            throw new IllegalArgumentException(
                    "用户名或密码错误"
            );
        }

        return new UserResponse(
                user.id(),
                user.username()
        );
    }

    public UserResponse getUserById(Long id) {

        User user = userRepository
                .findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("用户不存在")
                );

        return new UserResponse(
                user.id(),
                user.username()
        );
    }
}