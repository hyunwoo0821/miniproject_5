package com.aivle.backend.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users") // user 예약어 피하려고 s 붙임
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;       // 로그인 ID

    @Column(nullable = false)
    private String password;   // BCrypt로 암호화된 비번

    @Column(nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column
    private String refreshToken; // 현재 발급된 refresh token (로그아웃 시 null)

    public enum Role {
        USER, ADMIN
    }
}