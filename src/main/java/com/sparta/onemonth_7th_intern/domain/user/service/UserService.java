package com.sparta.onemonth_7th_intern.domain.user.service;

import com.sparta.onemonth_7th_intern.domain.user.dto.SigninRequestDto;
import com.sparta.onemonth_7th_intern.domain.user.dto.SignupRequestDto;
import com.sparta.onemonth_7th_intern.domain.user.dto.UserResponseDto;
import com.sparta.onemonth_7th_intern.domain.user.entity.User;
import com.sparta.onemonth_7th_intern.domain.user.enums.UserRole;
import com.sparta.onemonth_7th_intern.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupRequestDto signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        UserRole role = requestDto.getRole();

        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 username 입니다.");
        }

        User user = new User(requestDto, password, role);

        userRepository.save(user);

        return requestDto;
    }

    public Long signin(SigninRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀립니다.");
        }

        return user.getId();
    }
}