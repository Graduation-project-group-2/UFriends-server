package com.gachon.ufriendsserver.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class JoinDTO {
    @NotNull
    private String nickname;
    @NotNull
    private String email;
    @NotNull
    private String password;
}
