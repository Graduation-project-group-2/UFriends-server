package com.gachon.ufriendsserver.api.dto.member;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class MemberDTO {
    @NotNull
    private Integer memberId;
    @NotNull
    private String nickname;
    @NotNull
    private String email;
    @NotNull
    private String phoneNum;
    @NotNull
    private LocalDate birthday;
    @NotNull
    private LocalDate joinDate;
    @NotNull
    private String token;
}
