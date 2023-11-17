package com.wanted.budgetmgr.controller;

import com.wanted.budgetmgr.dto.LoginDTO;
import com.wanted.budgetmgr.dto.SignUpDTO;
import com.wanted.budgetmgr.dto.UserDTO;
import com.wanted.budgetmgr.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpDTO signUpDTO) {
        userService.save(signUpDTO);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        String token = userService.login(loginDTO);
        return ResponseEntity.ok("로그인에 성공했습니다.\naccessToken : " + token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshTokens(@RequestBody Map<String, String> tokenMap) {
        String refreshToken = tokenMap.get("refreshToken");
        Map<String, String> newTokenMap = userService.refreshTokens(refreshToken);
        return ResponseEntity.ok(newTokenMap);
    }
}
