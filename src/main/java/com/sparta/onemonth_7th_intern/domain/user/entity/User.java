package com.sparta.onemonth_7th_intern.domain.user.entity;

import com.sparta.onemonth_7th_intern.domain.user.dto.UserRequestDto;
import com.sparta.onemonth_7th_intern.domain.user.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    public User(UserRequestDto requestDto, String password, UserRole role) {
        this.username = requestDto.getUsername();
        this.nickname = requestDto.getNickname();
        this.password = password;
        this.role = role;
    }

}