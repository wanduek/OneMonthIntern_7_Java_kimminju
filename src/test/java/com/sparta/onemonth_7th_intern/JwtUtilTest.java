package com.sparta.onemonth_7th_intern;

import com.sparta.onemonth_7th_intern.config.jwt.JwtUtil;
import com.sparta.onemonth_7th_intern.domain.user.enums.UserRole;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JwtUtilTest {

    private JwtUtil jwtUtil;

    private static final String SECRET_KEY = "testSecretKey12345678901234567890"; // 최소 32자 이상
    private static final String TEST_USERNAME = "testusername";
    private static final UserRole TEST_ROLE = UserRole.USER;

    @BeforeAll
    public void init() {
        jwtUtil = new JwtUtil(SECRET_KEY); // 올바른 secretKey 전달
    }

    // Access Token 생성 및 검증 테스트
    @Test
    public void accessTokenTest() {
        // Access Token 생성
        String accessToken = jwtUtil.createAccessToken(TEST_USERNAME, TEST_ROLE);

        System.out.println("Access Token: " + accessToken);

        // 생성된 토큰 검증
        assertNotNull(accessToken, "Access token should not be null");
        assertTrue(jwtUtil.validateToken(accessToken), "Access token should be valid");

        // 토큰에서 사용자 정보 추출
        Claims claims = jwtUtil.getUserInfoFromToken(accessToken);

        assertEquals(TEST_USERNAME, claims.getSubject(), "Username should match");
        assertEquals(TEST_ROLE.toString(), claims.get("auth"), "Role should match");
    }

    // Refresh Token 생성 및 검증 테스트
    @Test
    public void refreshTokenTest() {
        // Refresh Token 생성
        String refreshToken = jwtUtil.createToken(TEST_USERNAME, TEST_ROLE, 7 * 24 * 60 * 60 * 1000L); // 7일 만료시간

        System.out.println("Refresh Token: " + refreshToken);

        // 생성된 토큰 검증
        assertNotNull(refreshToken, "Refresh token should not be null");
        assertTrue(jwtUtil.validateRefreshToken(refreshToken), "Refresh token should be valid");

        // 토큰에서 사용자 정보 추출
        Claims claims = jwtUtil.getUserInfoFromToken(refreshToken);

        assertEquals(TEST_USERNAME, claims.getSubject(), "Username should match");
        assertEquals(TEST_ROLE.toString(), claims.get("auth"), "Role should match");
    }
}