package com.sparta.onemonth_7th_intern.domain.token.repository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String refreshToken);
}
