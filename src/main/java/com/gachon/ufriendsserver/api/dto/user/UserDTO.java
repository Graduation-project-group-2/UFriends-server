package com.gachon.ufriendsserver.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDTO {
    private Integer userId;
    private String nickname;
    private String email;
    private String token;
    private LocalDate createdAt;
}
