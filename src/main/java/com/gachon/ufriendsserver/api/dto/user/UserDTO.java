package com.gachon.ufriendsserver.api.dto.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserDTO {
    private Integer userId;
    private String nickname;
    private String email;
    private String token;
    private LocalDate createdAt;
}
