package com.sparta.onemonth_7th_intern.domain.user.dto;

import com.sparta.onemonth_7th_intern.domain.user.enums.UserRole;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private UserRole userRole;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.userRole = user.getRole();
        this.createAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
    }
}