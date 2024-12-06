package com.sparta.onemonth_7th_intern;

import com.sparta.onemonth_7th_intern.domain.user.dto.SigninRequestDto;
import com.sparta.onemonth_7th_intern.domain.user.dto.SignupRequestDto;
import com.sparta.onemonth_7th_intern.domain.user.dto.UserResponseDto;
import com.sparta.onemonth_7th_intern.domain.user.entity.User;
import com.sparta.onemonth_7th_intern.domain.user.enums.UserRole;
import com.sparta.onemonth_7th_intern.domain.user.repository.UserRepository;
import com.sparta.onemonth_7th_intern.domain.user.service.UserService;
import com.sparta.onemonth_7th_intern.exception.CustomException;
import com.sparta.onemonth_7th_intern.exception.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void 회원가입_성공() {
        // given
        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setUsername("testname");
        requestDto.setNickname("testnickname");  // name 필드 설정
        requestDto.setPassword("password123!");  // 비밀번호 패턴에 맞춰 설정

        // 중복 유저네임 체크
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // 비밀번호 인코딩
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // User 객체 생성 및 Reflection으로 필드 설정
        User savedUser = new User();
        ReflectionTestUtils.setField(savedUser, "username", requestDto.getUsername());
        ReflectionTestUtils.setField(savedUser, "nickname", requestDto.getNickname());
        ReflectionTestUtils.setField(savedUser, "password", "encodedPassword");
        ReflectionTestUtils.setField(savedUser, "role", UserRole.USER);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // when
        UserResponseDto responseDto = userService.signup(requestDto);

        // then
        assertNotNull(responseDto);  // 응답이 null이 아닌지 확인
        assertEquals("testnickname", responseDto.getUsername());  // 이메일 확인
        verify(userRepository, times(1)).save(any(User.class));  // UserRepository의 save 메서드가 한 번 호출되었는지 확인
    }

    @Test
    void 로그인_성공() {
        // given
        // 기본 생성자를 사용하여 DTO 생성 후 필드 값 설정
        SigninRequestDto requestDto = new SigninRequestDto();
        requestDto.setUsername("testname");
        requestDto.setPassword("password123!");  // 패턴에 맞는 비밀번호 설정

        // User 객체 생성 및 Reflection으로 필드 설정
        User user = new User();
        ReflectionTestUtils.setField(user, "username", "test@example.com");
        ReflectionTestUtils.setField(user, "password", "encodedPassword");
        ReflectionTestUtils.setField(user, "role", UserRole.USER);

        // 가짜 데이터 반환 설정
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // when
        Long userId = userService.signin(requestDto);

        // then
        assertEquals(user.getId(), userId);  // 로그인 성공 시 사용자 ID 반환 확인
    }

    @Test
    void 비밀번호_틀림_예외_발생() {
        // given
        Long userId = 1L;
        String password = "wrongPassword";

        User user = new User();
        ReflectionTestUtils.setField(user, "username", "test@example.com");
        ReflectionTestUtils.setField(user, "password", "encodedPassword");
        ReflectionTestUtils.setField(user, "role", UserRole.USER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> userService.deleteUser(userId, password));

        // Reflection으로 errorCode를 직접 확인
        ErrorCode errorCode = (ErrorCode) ReflectionTestUtils.getField(exception, "errorCode");
        assertEquals(ErrorCode.INVALID_PASSWORD, errorCode);  // 비밀번호 틀릴 때 발생하는 예외 확인
    }

    @Test
    void 이미_비활성화된_사용자_삭제_예외_발생() {
        // given
        Long userId = 1L;
        String password = "password123!";

        User user = new User();
        ReflectionTestUtils.setField(user, "username", "testname");
        ReflectionTestUtils.setField(user, "password", "encodedPassword");
        ReflectionTestUtils.setField(user, "role", UserRole.USER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> userService.deleteUser(userId, password));

        // Reflection으로 errorCode를 직접 확인
        ErrorCode errorCode = (ErrorCode) ReflectionTestUtils.getField(exception, "errorCode");
        assertEquals(ErrorCode.ALREADY_DEACTIVATED_USER, errorCode);  // 이미 비활성화된 유저일 때 예외 발생 확인
    }
}
