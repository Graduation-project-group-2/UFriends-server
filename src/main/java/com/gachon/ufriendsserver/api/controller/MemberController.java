package com.gachon.ufriendsserver.api.controller;

import com.gachon.ufriendsserver.api.common.ResponseCode;
import com.gachon.ufriendsserver.api.common.config.TokenProvider;
import com.gachon.ufriendsserver.api.common.controller.CommonController;
import com.gachon.ufriendsserver.api.domain.Member;
import com.gachon.ufriendsserver.api.dto.member.JoinDTO;
import com.gachon.ufriendsserver.api.dto.member.LoginDTO;
import com.gachon.ufriendsserver.api.dto.member.MemberDTO;
import com.gachon.ufriendsserver.api.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

        joinDTO.setMemberId(member.getMemberId());
        joinDTO.setPassword(""); // 보안

        if(memberService.getMemberByMemberId(member.getMemberId()) != null)
            return SuccessReturn(joinDTO);

        return ErrorReturn(ResponseCode.SOMETHING_WRONG);
    }

    // 로그인
    @PostMapping("/member/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){

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



}
