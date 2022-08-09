package com.gachon.ufriendsserver.api.dto.member;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MemberDTO {
    private Integer memberId;
    private String nickname;
    private String email;
    private String phoneNum;
    private LocalDate birthday;
    private String token;
    private LocalDate joinDate;
}
