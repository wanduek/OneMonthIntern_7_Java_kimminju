package com.sparta.onemonth_7th_intern.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRequestDto {
    private String username;

    private String nickname;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[\\p{Punct}])[A-Za-z\\d\\p{Punct}]{8,20}$",
            message = "Password must be 8-20 characters long and include at least one letter, one number, and one special character.")
    private String password;


}
