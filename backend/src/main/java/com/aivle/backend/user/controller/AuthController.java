package com.aivle.backend.user.controller;

import com.aivle.backend.user.dto.*;
import com.aivle.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public void signup(@RequestBody SignupRequest request) {
        userService.signup(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    // refresh token 이용해서 access token 재발급
    @PostMapping("/reissue")
    public LoginResponse reissue(@RequestHeader("Refresh-Token") String refreshToken) {
        return userService.reissue(refreshToken);
    }

    // JWT 써서 현재 로그인한 유저 정보
    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
        return userService.getMyInfo(user.getUsername());
    }

    @PostMapping("/logout")
    public void logout(@AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
        userService.logout(user.getUsername());
    }
}
