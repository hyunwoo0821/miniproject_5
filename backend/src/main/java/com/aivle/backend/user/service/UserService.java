package com.aivle.backend.user.service;

import com.aivle.backend.config.JwtTokenProvider;
import com.aivle.backend.user.dto.*;
import com.aivle.backend.user.entity.User;
import com.aivle.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signup(SignupRequest req) {

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }
        if (userRepository.existsByNickname(req.getNickname())) {
            throw new RuntimeException("이미 사용 중인 닉네임입니다.");
        }

        User user = User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .nickname(req.getNickname())
                .role(User.Role.USER)
                .build();

        userRepository.save(user);
    }

    @Transactional
    public LoginResponse login(LoginRequest req) {

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("가입되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String access = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRole().name());
        String refresh = jwtTokenProvider.createRefreshToken();

        user.setRefreshToken(refresh); // DB에 저장
        return new LoginResponse(access, refresh);
    }

    @Transactional
    public LoginResponse reissue(String refreshToken) {

        User user = userRepository.findByEmail(
                        jwtTokenProvider.getEmailFromToken(refreshToken))
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new RuntimeException("유효하지 않은 리프레시 토큰입니다.");
        }

        String newAccess = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRole().name());
        String newRefresh = jwtTokenProvider.createRefreshToken();
        user.setRefreshToken(newRefresh);

        return new LoginResponse(newAccess, newRefresh);
    }

    @Transactional
    public void logout(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        user.setRefreshToken(null);
    }

    @Transactional(readOnly = true)
    public UserResponse getMyInfo(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return new UserResponse(user);
    }
}
