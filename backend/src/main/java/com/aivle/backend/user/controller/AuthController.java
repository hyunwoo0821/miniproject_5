package com.aivle.backend.user.controller;

import com.aivle.backend.config.JwtTokenProvider;
import com.aivle.backend.user.dto.LoginRequest;
import com.aivle.backend.user.dto.LoginResponse;
import com.aivle.backend.user.dto.SignupRequest;
import com.aivle.backend.user.dto.UpdateUserRequest;
import com.aivle.backend.user.dto.UserResponse;
import com.aivle.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    // ==========================
    // 회원가입
    // ==========================
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody SignupRequest request) {
        userService.signup(request);
        return ResponseEntity.ok().build();
    }

    // ==========================
    // 로그인
    // ==========================
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    // ==========================
    // 액세스 토큰 재발급
    // ==========================
    @PostMapping("/reissue")
    public ResponseEntity<LoginResponse> reissue(
            @RequestHeader("Refresh-Token") String refreshToken
    ) {
        LoginResponse response = userService.reissue(refreshToken);
        return ResponseEntity.ok(response);
    }

    // ==========================
    // 로그아웃
    // ==========================
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        String email = extractEmailFromHeaderOrThrow(authHeader);
        userService.logout(email);
        return ResponseEntity.ok().build();
    }

    // =====================================================
    // ===== 내 정보 조회 / 수정  =====
    // =====================================================

    // 1) 로그인한 유저 정보 조회
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        String email = extractEmailFromHeaderOrThrow(authHeader);
        UserResponse response = userService.getMyInfo(email);
        return ResponseEntity.ok(response);
    }

    // 2) 내 정보 수정
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMyInfo(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody UpdateUserRequest request
    ) {
        String email = extractEmailFromHeaderOrThrow(authHeader);
        UserResponse updated = userService.updateUser(email, request);
        return ResponseEntity.ok(updated);
    }

    // =====================================================
    // 토큰에서 email 추출 + 기본 에러 처리
    // =====================================================
    private String extractEmailFromHeaderOrThrow(String authHeader) {

        // Authorization 헤더가 없거나 형식이 잘못된 경우 → 401
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "로그인이 필요합니다."
            );
        }

        String token = authHeader.substring(7); // "Bearer " 이후 토큰 부분

        try {
            // ★ JwtTokenProvider 메소드 이름 맞춰주기
            return jwtTokenProvider.getEmailFromToken(token);
        } catch (Exception e) {
            // 토큰 파싱 중 에러 → 401
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "유효하지 않은 토큰입니다."
            );
        }
    }
}
