package com.gachon.ufriendsserver.api.controller;

import com.gachon.ufriendsserver.api.common.ResponseCode;
import com.gachon.ufriendsserver.api.common.controller.CommonController;
import com.gachon.ufriendsserver.api.common.security.TokenProvider;
import com.gachon.ufriendsserver.api.domain.User;
import com.gachon.ufriendsserver.api.dto.user.JoinDTO;
import com.gachon.ufriendsserver.api.dto.user.LoginDTO;
import com.gachon.ufriendsserver.api.dto.user.UserDTO;
import com.gachon.ufriendsserver.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController extends CommonController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginDTO loginDTO){
        User user = userService.login(loginDTO);

        if(user != null){
            String token = tokenProvider.create(user);
            UserDTO responseUserDTO = UserDTO.builder().email(user.getEmail()).userId(user.getUserId()).token(token).build();
            return SuccessReturn(responseUserDTO);
        } else {
            return ErrorReturn(ResponseCode.LOGIN_ERROR);
        }
    }

    // 이메일 중복 확인
    @PostMapping("/emailCheck/{email}")
    public ResponseEntity<?> emailCheck(@PathVariable String email){
        if(userService.isEmailExisting(email))
            return ErrorReturn(ResponseCode.DUPLICATE_DATA);
        return SuccessReturn();
    }

    // 닉네임 중복 확인
    @PostMapping("/nicknameCheck/{nickname}")
    public ResponseEntity<?> nicknameCheck(@PathVariable String nickname){
        if(userService.isNicknameExisting(nickname))
            return ErrorReturn(ResponseCode.DUPLICATE_DATA);
        return SuccessReturn();
    }

    // 전화번호 중복 확인
    @PostMapping("/phoneNumCheck/{phoneNum}")
    public ResponseEntity<?> phoneNumCheck(@PathVariable String phoneNum){
        String phoneNoUpdate = phoneNum.replaceAll("[^0-9]", "");
        if(userService.isPhoneNumExisting(phoneNoUpdate))
            return ErrorReturn(ResponseCode.DUPLICATE_DATA);
        return SuccessReturn();
    }

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<?> join(@Validated @RequestBody JoinDTO joinDTO, HttpServletResponse response) {
        if (userService.isEmailExisting(joinDTO.getEmail())) // Email 중복확인
            return ErrorReturn(ResponseCode.DUPLICATE_DATA);

        User user = userService.join(joinDTO);

        return SuccessReturn(user);
    }

    // 비밀번호 변경 - Account Page
    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Validated @RequestBody LoginDTO loginDTO) {
        // 사용자 확인 - 토큰 사용
        userService.changePassword(loginDTO.getEmail(), loginDTO.getPassword());
        return SuccessReturn();
    }

    // 회원탈퇴
    @DeleteMapping(value = {"/deleteUser/{userId}", "/deleteUser"})
    public ResponseEntity<?> deleteUser(@PathVariable(value = "userId") Integer userId) {
        userService.deleteUser(userId);
        return SuccessReturn();
    }


}
