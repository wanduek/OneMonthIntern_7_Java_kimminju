package com.sparta.onemonth_7th_intern.domain.user.controller;

import com.sparta.onemonth_7th_intern.domain.user.dto.SigninRequestDto;
import com.sparta.onemonth_7th_intern.domain.user.dto.UserRequestDto;
import com.sparta.onemonth_7th_intern.domain.user.dto.UserResponseDto;
import com.sparta.onemonth_7th_intern.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    /**
     * 회원 가입
     *
     * @param requestDto
     * @return 상태코드 200, UserResponseDto
     */
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody UserRequestDto requestDto) {
        return ResponseEntity.ok(userService.signup(requestDto));
    }

    /**
     * 로그인 코드
     *
     * @param requestDto
     * @return 상태코드 200, userId
     */
    @PostMapping("/signin")
    public ResponseEntity<Long> login(@RequestBody SigninRequestDto requestDto) {
        return ResponseEntity.ok(userService.login(requestDto));
    }
}
