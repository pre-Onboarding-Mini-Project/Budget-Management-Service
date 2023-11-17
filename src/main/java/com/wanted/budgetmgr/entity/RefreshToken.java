package com.wanted.budgetmgr.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryDatetime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Builder
    public RefreshToken(User user, String token, LocalDateTime expiryDatetime) {
        this.user = user;
        this.token = token;
        this.expiryDatetime = expiryDatetime;
    }
}
