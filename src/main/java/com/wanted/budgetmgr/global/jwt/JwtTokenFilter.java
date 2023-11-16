package com.wanted.budgetmgr.global.jwt;

import com.wanted.budgetmgr.service.RefreshTokenService;
import com.wanted.budgetmgr.service.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailService userDetailService;
    private final RefreshTokenService refreshTokenService;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, UserDetailService userDetailService, RefreshTokenService refreshTokenService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailService = userDetailService;
        this.refreshTokenService = refreshTokenService;
    }

    // 토큰 유효성 검사 및 사용자 인증
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extractTokenFromRequest(request, "Bearer ");

            // 토큰이 유효한 경우
            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {

                // 액세스 토큰이 만료되었을 때
                if (jwtTokenProvider.isTokenExpired(token)) {
                    String refreshToken = extractTokenFromRequest(request, "BearerRefresh ");

                    // 리프레시 토큰이 유효한 경우
                    if (StringUtils.hasText(refreshToken) && jwtTokenProvider.validateToken(refreshToken)) {
                        String email = jwtTokenProvider.getEmailFromToken(refreshToken);
                        String newAccessToken = jwtTokenProvider.createAccessToken(email);

                        response.setHeader("Authorization", "Bearer" + newAccessToken);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("토큰이 유효하지 않음", e);
        }
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request, String tokenPrefix) {
        // "Authorization" 헤더에서 토큰 추출
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(tokenPrefix)) {
            // "Bearer " 부분을 제외한 토큰 반환
            return bearerToken.substring(tokenPrefix.length());
        }

        // 유효한 토큰이 없는 경우
        return null;
    }
}
