package com.gachon.ufriendsserver.api.dto.member;

import com.gachon.ufriendsserver.api.domain.Social;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class LoginNaverDTO {
    @NotNull
    private int memberId;
    @NotNull
    private String nickname;
    @NotNull
    private String email;
    @NotNull
    private String phoneNum;
    @NotNull
    private LocalDate birthday;
    @NotNull
    private Social social;
}
