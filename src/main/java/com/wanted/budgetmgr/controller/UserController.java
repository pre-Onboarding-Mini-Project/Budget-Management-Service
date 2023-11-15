package com.wanted.budgetmgr.controller;

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

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserDTO userDTO) {
        userService.save(userDTO);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

}
