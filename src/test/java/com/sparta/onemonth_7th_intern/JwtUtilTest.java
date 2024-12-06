//package com.sparta.onemonth_7th_intern;
//
//import com.sparta.onemonth_7th_intern.config.jwt.JwtUtil;
//import com.sparta.onemonth_7th_intern.domain.user.enums.UserRole;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import static org.hamcrest.MatcherAssert.assertThat;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class JwtUtilTest {
//
//    private JwtUtil jwtUtil;
//
//    private static final String TEST_USERNAME = "testusername";
//    private static final UserRole TEST_ROLE = UserRole.USER;
//
//    @BeforeAll
//    public void init() {
//        jwtUtil = new JwtUtil(null);
//    }
//
//    // Access Token 생성 및 검증 테스트
//    @Test
//    public void accessTokenTest() {
//        // Access Token 생성
//        String accessToken = jwtUtil.createAccessToken(TEST_USERNAME, TEST_ROLE);
//
//        System.out.println("Access Token: " + accessToken);
//
//        // 생성된 토큰 검증
//        assertThat(accessToken).isNotNull();
//        assertThat(jwtUtil.validateToken(accessToken)).isTrue();
//
//        // 토큰에서 사용자 정보 추출
//        Claims claims = jwtUtil.getUserInfoFromToken(accessToken);
//
//        assertThat(claims.getSubject()).isEqualTo(TEST_USERNAME);
//        assertThat(claims.get("auth")).isEqualTo(TEST_ROLE.toString());
//    }
//
//    // Refresh Token 생성 및 검증 테스트
//    @Test
//    public void refreshTokenTest() {
//        // Refresh Token 생성
//        String refreshToken = jwtUtil.createToken(TEST_USERNAME, TEST_ROLE, 7 * 24 * 60 * 60 * 1000L); // 7일 만료시간
//
//        System.out.println("Refresh Token: " + refreshToken);
//
//        // 생성된 토큰 검증
//        assertThat(refreshToken).isNotNull();
//        assertThat(jwtUtil.validateRefreshToken(refreshToken)).isTrue();
//
//        // 토큰에서 사용자 정보 추출
//        Claims claims = jwtUtil.getUserInfoFromToken(refreshToken);
//
//        assertThat(claims.getSubject()).isEqualTo(TEST_USERNAME);
//        assertThat(claims.get("auth")).isEqualTo(TEST_ROLE.toString());
//    }
//}