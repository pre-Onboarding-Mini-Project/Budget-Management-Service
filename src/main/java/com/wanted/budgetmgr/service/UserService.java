package com.wanted.budgetmgr.service;

import com.wanted.budgetmgr.dto.LoginDTO;
import com.wanted.budgetmgr.dto.SignUpDTO;
import com.wanted.budgetmgr.entity.User;
import com.wanted.budgetmgr.global.exception.CustomException;
import com.wanted.budgetmgr.global.jwt.JwtTokenProvider;
import com.wanted.budgetmgr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    public User save(SignUpDTO signUpDTO) {
        if (userRepository.findByEmail(signUpDTO.getEmail()).isPresent()) {
            throw CustomException.duplicateEmail(signUpDTO.getEmail());
        }

        User user = User.builder()
                .name(signUpDTO.getName())
                .email(signUpDTO.getEmail())
                .password(bCryptPasswordEncoder.encode(signUpDTO.getPassword()))
                .createAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    public String login(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if(bCryptPasswordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            refreshTokenService.createRefreshToken(user);

            return jwtTokenProvider.createAccessToken(user.getEmail());
        } else {
            throw new BadCredentialsException("잘못된 패스워드");
        }
    }

    public Map<String, String> refreshTokens(String refreshToken) {
        if (StringUtils.hasText(refreshToken) && jwtTokenProvider.validateToken(refreshToken)) {
            String email = jwtTokenProvider.getEmailFromToken(refreshToken);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

            refreshTokenService.deleteByUser(user);

            refreshTokenService.createRefreshToken(user);

            String newAccessToken = jwtTokenProvider.createAccessToken(user.getEmail());
            String newRefreshToken = refreshTokenService.createRefreshToken(user).getToken();

            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put("accessToken", newAccessToken);
            tokenMap.put("refreshToken", newRefreshToken);

            return tokenMap;
        } else {
            throw new BadCredentialsException("잘못된 리프레시 토큰입니다.");
        }
    }
}
