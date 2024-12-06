package com.sparta.onemonth_7th_intern.domain.user.dto;

import com.sparta.onemonth_7th_intern.domain.user.entity.User;
import com.sparta.onemonth_7th_intern.domain.user.enums.UserRole;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    private String username;

    private String nickname;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[\\p{Punct}])[A-Za-z\\d\\p{Punct}]{8,20}$",
            message = "Password must be 8-20 characters long and include at least one letter, one number, and one special character.")
    private String password;

    private UserRole role;


}
