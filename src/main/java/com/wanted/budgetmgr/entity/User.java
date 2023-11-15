package com.wanted.budgetmgr.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @CreatedDate
    @Column(name = "create_at")
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public User(String name, String email, String password, LocalDateTime createAt, LocalDateTime updatedAt) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.createAt = createAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return  List.of(new SimpleGrantedAuthority("user"));
    }

    // 사용자 id 반환
    @Override
    public String getUsername() {
        return email;
    }

    // 사용자 pw 반환
    @Override
    public String getPassword() {
        return password;
    }

    // 계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        return true; //true -> 만료X
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        return true; //true -> 잠금X
    }

    // 패스워드의 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        return true; //true -> 만료X
    }

    // 계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        return true; //true -> 사용가능
    }
}
