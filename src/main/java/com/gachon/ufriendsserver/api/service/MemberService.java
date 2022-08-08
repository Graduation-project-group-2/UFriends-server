package com.gachon.ufriendsserver.api.service;

import com.gachon.ufriendsserver.api.common.ResponseCode;
import com.gachon.ufriendsserver.api.domain.Member;
import com.gachon.ufriendsserver.api.dto.member.JoinDTO;
import com.gachon.ufriendsserver.api.dto.member.LoginDTO;
import com.gachon.ufriendsserver.api.dto.member.LoginNaverDTO;
import com.gachon.ufriendsserver.api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean isEmailExisting(String email){
        return memberRepository.findByEmail(email).isPresent();
    }

    public boolean isNicknameExisting(String nickname){
        return memberRepository.findByNickname(nickname).isPresent();
    }

    public Member join(JoinDTO joinDTO){

        if(!isEmailExisting(joinDTO.getEmail()) && !isNicknameExisting(joinDTO.getNickname())){
            String encodedPassword = passwordEncoder.encode(joinDTO.getPassword());
            joinDTO.setPassword(encodedPassword);

            Member member = Member.builder()
                    .nickname(joinDTO.getNickname())
                    .email(joinDTO.getEmail())
                    .password(joinDTO.getPassword())
                    .phoneNum(joinDTO.getPhoneNum())
                    .birthday(joinDTO.getBirthday())
                    .build();

            memberRepository.save(member);

            return getMemberByEmail(joinDTO.getEmail());
        }

        return null;
    }

    public Member login(LoginDTO loginDTO){
        String encodedPassword = passwordEncoder.encode(loginDTO.getPassword());
        loginDTO.setPassword(encodedPassword);

        Member member = getMemberByEmail(loginDTO.getEmail());
        if(member != null){
            if(member.getPassword() == loginDTO.getPassword())
                return member;
        }

        return member;
    }

    public Member loginNaver(LoginNaverDTO loginNaverDTO){
        Member member = Member.builder()
                .nickname(loginNaverDTO.getNickname())
                .email(loginNaverDTO.getEmail())
                .password("")
                .phoneNum(loginNaverDTO.getPhoneNum())
                .birthday(loginNaverDTO.getBirthday())
                .build();

        memberRepository.save(member);

        return getMemberByEmail(loginNaverDTO.getEmail());
    }

    public Member getMemberByMemberId(int memberId){
        return memberRepository.findByMemberId(memberId).orElseThrow(() -> new NullPointerException(ResponseCode.NOT_FOUND_DATA.getMsg()));
    }

    public Member getMemberByEmail(String email){
        return memberRepository.findByEmail(email).orElseThrow(() -> new NullPointerException(ResponseCode.NOT_FOUND_DATA.getMsg()));
    }

    public void deleteUserByEmail(String email){
        memberRepository.delete(memberRepository.findByEmail(email).orElseThrow(() -> new NullPointerException(ResponseCode.NOT_FOUND_DATA.getMsg())));
    }


}
