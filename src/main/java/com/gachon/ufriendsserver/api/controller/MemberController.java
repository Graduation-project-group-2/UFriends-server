package com.gachon.ufriendsserver.api.controller;

import com.gachon.ufriendsserver.api.common.ResponseCode;
import com.gachon.ufriendsserver.api.common.config.TokenProvider;
import com.gachon.ufriendsserver.api.common.controller.CommonController;
import com.gachon.ufriendsserver.api.domain.Member;
import com.gachon.ufriendsserver.api.domain.Social;
import com.gachon.ufriendsserver.api.dto.member.JoinDTO;
import com.gachon.ufriendsserver.api.dto.member.LoginDTO;
import com.gachon.ufriendsserver.api.dto.member.LoginNaverDTO;
import com.gachon.ufriendsserver.api.dto.member.MemberDTO;
import com.gachon.ufriendsserver.api.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController extends CommonController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @GetMapping("/test/hello")
    public String hello(){
        return "Hello, U-Friends\n리액트, 스프링부트 연결 테스트";
    }

    // 이메일 중복 확인
    @GetMapping("/member/emailValid")
    public ResponseEntity<?> emailValid(@RequestParam String email){
        if(memberService.isEmailExisting(email))
            return ErrorReturn(ResponseCode.DUPLICATE_DATA);
        return SuccessReturn();
    }

    // 닉네임 중복 확인
    @GetMapping("/member/nicknameValid")
    public ResponseEntity<?> nicknameValid(@RequestParam String nickname){
        if(memberService.isNicknameExisting(nickname))
            return ErrorReturn(ResponseCode.DUPLICATE_DATA);
        return SuccessReturn();
    }

    // 회원가입
    @PostMapping("/member/join")
    public ResponseEntity<?> join(@RequestBody JoinDTO joinDTO){
        String phoneNoUpdate = joinDTO.getPhoneNum().replaceAll("[^0-9]", "");
        joinDTO.setPhoneNum(phoneNoUpdate);

        Member member = memberService.join(joinDTO);

        if(member == null)
            return ErrorReturn(ResponseCode.DUPLICATE_DATA);

        if(memberService.getMemberByMemberId(member.getMemberId()) != null){
            joinDTO.setMemberId(member.getMemberId());
            joinDTO.setPassword(""); // 보안
            return SuccessReturn(joinDTO);
        }
        return ErrorReturn(ResponseCode.UNKNOWN_ERROR);
    }

    // 로그인
    @PostMapping("/member/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginDTO loginDTO){

        Member member = memberService.login(loginDTO);

        if(member != null){
            String token = tokenProvider.create(member);

            MemberDTO memberDTO = MemberDTO.builder()
                    .memberId(member.getMemberId())
                    .email(member.getEmail())
                    .nickname(member.getNickname())
                    .phoneNum(member.getPhoneNum())
                    .birthday(member.getBirthday())
                    .joinDate(member.getJoinDate())
                    .token(token)
                    .build();

            return SuccessReturn(memberDTO);
        }

        return ErrorReturn(ResponseCode.NOT_FOUND_DATA);
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
