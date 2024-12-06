package com.sparta.onemonth_7th_intern.config.jwt;

import com.sparta.onemonth_7th_intern.config.userdetail.UserDetailsImpl;
import com.sparta.onemonth_7th_intern.config.userdetail.UserDetailsServiceImpl;
import com.sparta.onemonth_7th_intern.domain.token.entity.RefreshToken;
import com.sparta.onemonth_7th_intern.domain.token.repository.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        // 로그인과 회원가입 요청은 이 필터를 건너뜁니다.
        String path = req.getRequestURI();
        if (path.equals("/api/users/login") || path.equals("/apiusers/signup")) {
            filterChain.doFilter(req, res);
            return;
        }

        String accessToken = jwtUtil.getJwtFromHeader(req);
        log.info("Extracted accessToken from header: {}", accessToken);
        Optional<String> refreshTokenOpt = jwtUtil.getRefreshTokenFromCooke(req);
        String refreshToken = refreshTokenOpt.get();
        log.info("Extracted accessToken from header: {}", refreshToken);

        if (StringUtils.hasText(accessToken)) {
            if (jwtUtil.validateToken(accessToken)) {
                setAuthentication(jwtUtil.getUserInfoFromToken(accessToken).getSubject());
            } else if (StringUtils.hasText(refreshToken) && jwtUtil.validateRefreshToken(refreshToken)) {
                // AccessToken 이 만료되었지만 RefreshToken 이 유효한 경우
                Optional<RefreshToken> tokenOpt = refreshTokenRepository.findByToken(refreshToken);
                if (tokenOpt.isPresent()) {
                    RefreshToken token = tokenOpt.get();
                    String username = token.getUser().getUsername();
                    UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

                    String newAccessToken = jwtUtil.createAccessToken(username, userDetails.getUser().getRole());
                    res.addHeader(JwtUtil.AUTHORIZATION_HEADER, JwtUtil.BEARER_PREFIX + newAccessToken);

                    jwtUtil.updateRefreshToken(token);
                    jwtUtil.setRefreshTokenCookie(res, token.getToken());

                    setAuthentication(username);
                }
            }

            filterChain.doFilter(req, res);
        }
    }

    // 인증 처리
    public void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(email);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}