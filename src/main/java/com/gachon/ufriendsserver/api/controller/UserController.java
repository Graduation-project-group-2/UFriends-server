package com.gachon.ufriendsserver.api.controller;

import com.gachon.ufriendsserver.api.common.ResponseCode;
import com.gachon.ufriendsserver.api.common.controller.CommonController;
import com.gachon.ufriendsserver.api.common.dto.ResponseDTO;
import com.gachon.ufriendsserver.api.domain.User;
import com.gachon.ufriendsserver.api.dto.JoinDTO;
import com.gachon.ufriendsserver.api.dto.LoginDTO;
import com.gachon.ufriendsserver.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController extends CommonController {
    private final UserService userService;

    // 이메일 중복 확인
    @GetMapping("/user/emailValid")
    public ResponseEntity<?> emailValid(@RequestParam String email){
        if(userService.isEmailExisting(email))
            return SuccessReturn();
        return ErrorReturn(ResponseCode.DUPLICATE_DATA);
    }

    // 닉네임 중복 확인
    @GetMapping("/user/nicknameValid")
    public ResponseEntity<?> nicknameValid(@RequestParam String nickname){
        if(userService.isNicknameExisting(nickname))
            return SuccessReturn();
        return ErrorReturn(ResponseCode.DUPLICATE_DATA);
    }

    // 회원가입
    @PostMapping("/user/join")
    public ResponseEntity<?> join(@Valid JoinDTO joinDTO){
        String phoneNoUpdate = joinDTO.getPhoneNum().replaceAll("[^0-9]", "");
        joinDTO.setPhoneNum(phoneNoUpdate);
        User user = userService.join(joinDTO);

        return SuccessReturn(user);
    }

    // 로그인
    @PostMapping("/user/login")
    public ResponseEntity<?> login(@Valid LoginDTO loginDTO){
        User user = userService.login(loginDTO);
        if(user != null)
            return SuccessReturn();

        return ErrorReturn(ResponseCode.NOT_FOUND_DATA);
    }

    // 비밀번호 찾기



}
