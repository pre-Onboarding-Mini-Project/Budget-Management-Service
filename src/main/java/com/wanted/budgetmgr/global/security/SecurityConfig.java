package com.wanted.budgetmgr.global.security;

import com.wanted.budgetmgr.global.jwt.JwtConfigurer;
import com.wanted.budgetmgr.service.RefreshTokenService;
import com.wanted.budgetmgr.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final UserDetailService userService;
    private final RefreshTokenService refreshTokenService;

    // 시큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers("/static/**");
    }

    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests()
            .requestMatchers("/signup","/login","/refresh").permitAll()
            .anyRequest().authenticated()
            .and()
            .csrf().disable() //csrf 비활성화 (학습용 프로젝트이므로 편의를 위해 임시설정)
            .apply(jwtConfigurer())
            .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");

        return http.build();
    }

    // JWT 관련 구성 추가
    private JwtConfigurer jwtConfigurer() throws Exception {
        return new JwtConfigurer(userService, refreshTokenService);
    }

    // 인증 관리자 관련 설정
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
        BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userService)
        throws Exception {
            return http.getSharedObject(AuthenticationManagerBuilder.class)
                    .userDetailsService(userService)
                    .passwordEncoder(bCryptPasswordEncoder)
                    .and()
                    .build();
    }

    // pw 인코더로 사용할 빈 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
