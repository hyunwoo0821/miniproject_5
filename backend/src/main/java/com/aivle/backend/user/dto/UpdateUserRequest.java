package com.aivle.backend.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 정보 수정 요청 DTO
 * - currentPassword : 현재 비밀번호 (검증용)
 * - newPassword     : 변경할 비밀번호
 * - nickname        : 변경할 닉네임
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {

    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private String currentPassword;   // 현재 비밀번호

    private String newPassword;       // 새 비밀번호

    private String nickname;         // 새 닉네임
}
