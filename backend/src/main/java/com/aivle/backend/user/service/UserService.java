package com.aivle.backend.user.service;

import com.aivle.backend.config.JwtTokenProvider;
import com.aivle.backend.user.dto.LoginRequest;
import com.aivle.backend.user.dto.LoginResponse;
import com.aivle.backend.user.dto.SignupRequest;
import com.aivle.backend.user.dto.UpdateUserRequest;
import com.aivle.backend.user.dto.UserResponse;
import com.aivle.backend.user.entity.User;
import com.aivle.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // ==========================
    // 회원가입
    // ==========================
    @Transactional
    public void signup(SignupRequest req) {

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "이미 사용 중인 이메일입니다."
            );
        }

        if (userRepository.existsByNickname(req.getNickname())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "이미 사용 중인 닉네임입니다."
            );
        }

        User user = User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .nickname(req.getNickname())
                // ★ 여기! role 은 요청에서 받지 않고 USER 로 고정
                .role(User.Role.USER)
                .build();

        userRepository.save(user);
    }

    // ==========================
    // 로그인
    // ==========================
    @Transactional
    public LoginResponse login(LoginRequest req) {

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "이메일 또는 비밀번호가 올바르지 않습니다."
                ));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "이메일 또는 비밀번호가 올바르지 않습니다."
            );
        }

        String accessToken = jwtTokenProvider.createAccessToken(
                user.getEmail(),
                user.getRole().name()
        );
        String refreshToken = jwtTokenProvider.createRefreshToken();

        user.setRefreshToken(refreshToken);

        return new LoginResponse(accessToken, refreshToken);
    }

    // ==========================
    // 토큰 재발급
    // ==========================
    @Transactional
    public LoginResponse reissue(String refreshToken) {

        // 간단 구현: 모든 유저 중에서 refreshToken 이 같은 사람 찾기
        User user = userRepository.findAll().stream()
                .filter(u -> refreshToken.equals(u.getRefreshToken()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "유효하지 않은 리프레시 토큰입니다."
                ));

        String newAccessToken = jwtTokenProvider.createAccessToken(
                user.getEmail(),
                user.getRole().name()
        );
        String newRefreshToken = jwtTokenProvider.createRefreshToken();

        user.setRefreshToken(newRefreshToken);

        return new LoginResponse(newAccessToken, newRefreshToken);
    }

    // ==========================
    // 로그아웃 (리프레시 토큰 제거)
    // ==========================
    @Transactional
    public void logout(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "사용자를 찾을 수 없습니다."
                ));

        user.setRefreshToken(null);
    }

    // ==========================
    // 내 정보 조회
    // ==========================
    @Transactional(readOnly = true)
    public UserResponse getMyInfo(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "사용자를 찾을 수 없습니다. 다시 로그인 해주세요."
                ));

        return new UserResponse(user);
    }

    // ==========================
    // 내 정보 수정
    //  - 현재 비밀번호 확인
    //  - 새 비밀번호 / 닉네임 변경
    // ==========================
    @Transactional
    public UserResponse updateUser(String email, UpdateUserRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "사용자를 찾을 수 없습니다. 다시 로그인 해주세요."
                ));

        // 1) 현재 비밀번호 검사
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "현재 비밀번호가 일치하지 않습니다."
            );
        }

        // 2) 새 비밀번호가 있으면 변경
        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        // 3) 닉네임 변경 (중복 체크 포함)
        if (request.getNickname() != null && !request.getNickname().isBlank()) {
            if (userRepository.existsByNickname(request.getNickname())
                    && !request.getNickname().equals(user.getNickname())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "이미 사용 중인 닉네임입니다."
                );
            }
            user.setNickname(request.getNickname());
        }

        // 4) 변경된 내용으로 응답 DTO 생성
        return new UserResponse(user);
    }
}
