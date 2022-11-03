package com.gachon.ufriendsserver.api.service;

import com.gachon.ufriendsserver.api.common.ResponseCode;
import com.gachon.ufriendsserver.api.common.security.TokenProvider;
import com.gachon.ufriendsserver.api.domain.Member;
import com.gachon.ufriendsserver.api.dto.member.JoinDTO;
import com.gachon.ufriendsserver.api.dto.member.LoginDTO;
import com.gachon.ufriendsserver.api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    @Autowired
    private TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public Member getUserByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NullPointerException("NOT FOUND USER"));
    }

    public boolean isEmailExisting(String email){
        return memberRepository.findByEmail(email).isPresent();
    }

    public boolean isNicknameExisting(String nickname){
        return memberRepository.findByNickname(nickname).isPresent();
    }

    public boolean isPhoneNumExisting(String phoneNum){
        return memberRepository.findByPhoneNum(phoneNum).isPresent();
    }

    public Member join(JoinDTO joinDTO){

        if(!isEmailExisting(joinDTO.getEmail()) && !isNicknameExisting(joinDTO.getNickname())){
            joinDTO.setPassword(passwordEncoder.encode(joinDTO.getPassword()));

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

    public Member getByCredentials(final String email, final String password, final PasswordEncoder encoder){
        final Member originalMember = getMemberByEmail(email);
        System.out.println(password);
        if(originalMember != null && encoder.matches(password, originalMember.getPassword())){
            return originalMember;
        }
        return null;
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
