package com.wanted.budgetmgr.service;

import com.wanted.budgetmgr.entity.RefreshToken;
import com.wanted.budgetmgr.entity.User;
import com.wanted.budgetmgr.global.jwt.JwtTokenProvider;
import com.wanted.budgetmgr.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RefreshTokenService {

    @Value("${jwt.refreshExpiration}")
    private Long refreshExpiration;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public RefreshToken createRefreshToken(User user) {
        Optional<RefreshToken> existToken = refreshTokenRepository.findByUser(user);
        RefreshToken refreshToken;

        refreshToken = existToken.orElseGet(() -> RefreshToken.builder()
                .user(user)
                .token(jwtTokenProvider.createToken(user.getEmail(), refreshExpiration))
                .expiryDatetime(LocalDateTime.now().plusMinutes(refreshExpiration))
                .build());

        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
