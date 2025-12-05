package com.aivle.backend.user.dto;

import com.aivle.backend.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponse {
    private Long id;
    private String email;
    private String nickname;

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
    }
}