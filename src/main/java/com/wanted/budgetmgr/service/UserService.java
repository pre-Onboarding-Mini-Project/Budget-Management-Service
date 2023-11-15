package com.wanted.budgetmgr.service;

import com.wanted.budgetmgr.dto.UserDTO;
import com.wanted.budgetmgr.entity.User;
import com.wanted.budgetmgr.global.exception.CustomException;
import com.wanted.budgetmgr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User save(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw CustomException.duplicateEmail(userDTO.getEmail());
        }

        User user = User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .password(bCryptPasswordEncoder.encode(userDTO.getPassword()))
                .createAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }
}
