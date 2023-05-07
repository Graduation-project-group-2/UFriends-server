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
    @GetMapping("/email")
    public ResponseEntity<?> emailCheck(@RequestParam String email){
        if(userService.isEmailExisting(email))
            return ErrorReturn(ResponseCode.DUPLICATE_DATA);
        return SuccessReturn();
    }

    // 닉네임 중복 확인
    @GetMapping("/nickname")
    public ResponseEntity<?> nicknameCheck(@RequestParam String nickname){
        if(userService.isNicknameExisting(nickname))
            return ErrorReturn(ResponseCode.DUPLICATE_DATA);
        return SuccessReturn();
    }

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<?> join(@Validated @RequestBody JoinDTO joinDTO) {
        return SuccessReturn(userService.join(joinDTO).setPassword(""));
    }

    // 로그인 후 또는 인증 후 비밀번호 변경
    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@Validated @RequestBody LoginDTO loginDTO) {
        userService.changePassword(loginDTO.getEmail(), loginDTO.getPassword());
        return SuccessReturn();
    }

    // 로그인 전 비밀번호 변경(찾기)을 위하여 이메일과 닉네임 인증 받기
    @GetMapping("/checkUser")
    public ResponseEntity<?> resetPassword(@RequestParam String email, @RequestParam String nickname){
        User user = userService.resetPassword(email, nickname);
        if(user == null){
            return ErrorReturn(ResponseCode.NOT_FOUND_DATA);
        }
        return SuccessReturn(user);
    }

    @GetMapping("/myAccount")
    public ResponseEntity<?> getUserInfo(@RequestParam Integer userId){
        User user = userService.getUserById(userId);
        return SuccessReturn(user.setPassword(""));
    }

    // 회원탈퇴
    @DeleteMapping(value = {"/{userId}"})
    public ResponseEntity<?> deleteUser(@PathVariable(value = "userId") Integer userId) {
        userService.deleteUser(userId);
        return SuccessReturn();
    }

}
