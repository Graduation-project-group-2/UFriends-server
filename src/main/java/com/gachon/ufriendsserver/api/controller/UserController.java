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

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginDTO loginDTO){
        User user = userService.login(loginDTO);

        if(user != null){
            String token = tokenProvider.create(user);
            UserDTO responseUserDTO = UserDTO.builder().email(user.getEmail()).nickname(user.getNickname()).userId(user.getUserId()).token(token).createdAt(user.getCreatedAt()).build();
            return SuccessReturn(responseUserDTO);
        } else {
            return ErrorReturn(ResponseCode.LOGIN_ERROR);
        }
    }

    // 이메일 중복 확인
    @GetMapping("/emailCheck/{email}")
    public ResponseEntity<?> emailCheck(@PathVariable String email){
        if(userService.isEmailExisting(email))
            return ErrorReturn(ResponseCode.DUPLICATE_DATA);
        return SuccessReturn();
    }

    // 닉네임 중복 확인
    @GetMapping("/nicknameCheck/{nickname}")
    public ResponseEntity<?> nicknameCheck(@PathVariable String nickname){
        if(userService.isNicknameExisting(nickname))
            return ErrorReturn(ResponseCode.DUPLICATE_DATA);
        return SuccessReturn();
    }

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<?> join(@Validated @RequestBody JoinDTO joinDTO) {
        return SuccessReturn(userService.join(joinDTO).setPassword(""));
    }

    // 비밀번호 변경 - Account Page
    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Validated @RequestBody LoginDTO loginDTO) {
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
