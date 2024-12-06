package com.sparta.onemonth_7th_intern.domain.user.dto;

import com.sparta.onemonth_7th_intern.domain.user.entity.User;
import com.sparta.onemonth_7th_intern.domain.user.enums.UserRole;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private UserRole userRole;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.userRole = user.getRole();
    }
}