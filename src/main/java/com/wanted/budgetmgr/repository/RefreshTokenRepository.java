package com.wanted.budgetmgr.repository;

import com.wanted.budgetmgr.entity.RefreshToken;
import com.wanted.budgetmgr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
    void deleteByUser(User user);
}
