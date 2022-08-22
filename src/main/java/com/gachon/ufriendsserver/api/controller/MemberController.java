package com.gachon.ufriendsserver.api.controller;

import com.gachon.ufriendsserver.api.common.ResponseCode;
import com.gachon.ufriendsserver.api.common.controller.CommonController;
import com.gachon.ufriendsserver.api.common.security.TokenProvider;
import com.gachon.ufriendsserver.api.domain.Member;
import com.gachon.ufriendsserver.api.domain.Social;
import com.gachon.ufriendsserver.api.dto.member.JoinDTO;
import com.gachon.ufriendsserver.api.dto.member.LoginDTO;
import com.gachon.ufriendsserver.api.dto.member.LoginNaverDTO;
import com.gachon.ufriendsserver.api.dto.member.MemberDTO;
import com.gachon.ufriendsserver.api.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController extends CommonController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/hello")
    public String hello(){
        return "Hello, U-Friends. Jenkins 테스트 중입니다.";
    }

    // 이메일 중복 확인
    @GetMapping("/emailValid")
    public ResponseEntity<?> emailValid(@RequestParam String email){
        if(memberService.isEmailExisting(email))
            return ErrorReturn(ResponseCode.DUPLICATE_DATA);
        return SuccessReturn();
    }

    // 닉네임 중복 확인
    @GetMapping("/nicknameValid")
    public ResponseEntity<?> nicknameValid(@RequestParam String nickname){
        if(memberService.isNicknameExisting(nickname))
            return ErrorReturn(ResponseCode.DUPLICATE_DATA);
        return SuccessReturn();
    }

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<?> join(@Validated @RequestBody JoinDTO joinDTO, HttpServletResponse response){
        String phoneNoUpdate = joinDTO.getPhoneNum().replaceAll("[^0-9]", "");
        joinDTO.setPhoneNum(phoneNoUpdate);
        joinDTO.setPassword(passwordEncoder.encode(joinDTO.getPassword()));

        Member member = memberService.join(joinDTO);

        MemberDTO memberDTO = MemberDTO.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .birthday(member.getBirthday())
                .phoneNum(member.getPhoneNum())
                .joinDate(member.getJoinDate())
                .build();

        return SuccessReturn(memberDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@Validated @RequestBody LoginDTO loginDTO){
        Member member = memberService.getByCredentials(loginDTO.getEmail(), loginDTO.getPassword(), passwordEncoder);

        if(member != null){
            String token = tokenProvider.create(member);

            MemberDTO memberDTO = MemberDTO.builder()
                    .memberId(member.getMemberId())
                    .email(member.getEmail())
                    .nickname(member.getNickname())
                    .birthday(member.getBirthday())
                    .phoneNum(member.getPhoneNum())
                    .token(token)
                    .joinDate(member.getJoinDate())
                    .build();

            return SuccessReturn(memberDTO);
        } else {
            return ErrorReturn(ResponseCode.LOGIN_ERROR);
        }
    }


    // 비밀번호 찾기

    // 네이버 로그인
    @PostMapping("/member/login/naver")
    public ResponseEntity<?> naverLogin(@RequestParam("n_birthday") String n_birthday,
                             @RequestParam("n_birthyear") String n_birthyear,
                             @RequestParam("n_email") String n_email,
                             @RequestParam("n_id") String n_id,
                             @RequestParam("n_nickName") String n_nickName,
                             @RequestParam("n_phoneNum") String n_phoneNum) {

        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        String birthday = n_birthday + "-" + n_birthyear;

        LoginNaverDTO loginNaverDTO = LoginNaverDTO.builder()
                .memberId(Integer.parseInt(n_id))
                .email(n_email)
                .nickname(n_nickName)
                .phoneNum(n_phoneNum)
                .birthday(LocalDate.parse(birthday, inputFormat))
                .social(Social.NAVER)
                .build();

        Member member = memberService.loginNaver(loginNaverDTO);

        if(member != null)
            return SuccessReturn(loginNaverDTO);

        return ErrorReturn(ResponseCode.NAVER_LOGIN_ERROR);
    }

    // 카카오 로그인

    // 구글 로그인


}
