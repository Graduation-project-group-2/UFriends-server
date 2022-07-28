package com.gachon.ufriendsserver.api.service;

import com.gachon.ufriendsserver.api.domain.Member;
import com.gachon.ufriendsserver.api.dto.JoinDTO;
import com.gachon.ufriendsserver.api.dto.LoginDTO;
import com.gachon.ufriendsserver.api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

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
        String encodedPassword = passwordEncoder.encode(joinDTO.getPassword());
        joinDTO.setPassword(encodedPassword);

        Member member = Member.builder()
                .nickname(joinDTO.getNickname())
                .email(joinDTO.getEmail())
                .password(joinDTO.getPassword())
                .phoneNum(joinDTO.getPhoneNum())
                .birthday(joinDTO.getBirthday())
                .build();

        return memberRepository.save(member);
    }

    public Member login(LoginDTO loginDTO){
        String encodedPassword = passwordEncoder.encode(loginDTO.getPassword());
        loginDTO.setPassword(encodedPassword);

        Member member = memberRepository.findByEmail(loginDTO.getEmail()).orElseThrow(() -> new NullPointerException("NOT_FOUND_DATA"));

        boolean isUser = Objects.equals(member.getPassword(), loginDTO.getPassword());

        if(isUser)
            return member;
        else
            return null;
    }

    public Member getMemberByMemberId(int memberId){
        return memberRepository.findByMemberId(memberId).get();
    }

    public void deleteUserByEmail(String email){
        memberRepository.delete(memberRepository.findByEmail(email).get());
    }

}
