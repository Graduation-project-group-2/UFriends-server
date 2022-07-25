package com.gachon.ufriendsserver.api.dto;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class JoinDTO {
    @NotNull
    private int userId;
    @NotNull
    private String nickname;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String phoneNum;
    @NotNull
    private LocalDate birthday;

}
