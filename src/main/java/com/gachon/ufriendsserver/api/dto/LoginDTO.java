package com.gachon.ufriendsserver.api.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class LoginDTO {
    @NotNull
    private String email;
    @NotNull
    private String password;
}
