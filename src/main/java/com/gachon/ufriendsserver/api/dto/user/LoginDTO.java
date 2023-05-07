package com.gachon.ufriendsserver.api.dto.user;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class LoginDTO {
    @NotNull
    private String email;
    @NotNull
    private String password;
}
