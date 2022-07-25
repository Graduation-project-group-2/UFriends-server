package com.gachon.ufriendsserver.api.service;

import com.gachon.ufriendsserver.api.common.ResponseCode;
import com.gachon.ufriendsserver.api.domain.User;
import com.gachon.ufriendsserver.api.dto.JoinDTO;
import com.gachon.ufriendsserver.api.dto.LoginDTO;
import com.gachon.ufriendsserver.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean isEmailExisting(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean isNicknameExisting(String nickname){
        return userRepository.findByNickname(nickname).isPresent();
    }

    public User join(JoinDTO joinDTO){
        String encodedPassword = passwordEncoder.encode(joinDTO.getPassword());
        joinDTO.setPassword(encodedPassword);

        User user = User.builder()
                .nickname(joinDTO.getNickname())
                .email(joinDTO.getEmail())
                .password(joinDTO.getPassword())
                .phoneNum(joinDTO.getPhoneNum())
                .birthday(joinDTO.getBirthday())
                .build();

        return userRepository.save(user);
    }

    public User login(LoginDTO loginDTO){
        String encodedPassword = passwordEncoder.encode(loginDTO.getPassword());
        loginDTO.setPassword(encodedPassword);

        User user = userRepository.findByEmail(loginDTO.getEmail()).orElseThrow(() -> new NullPointerException("NOT_FOUND_DATA"));

        boolean isUser = Objects.equals(user.getPassword(), loginDTO.getPassword());

        if(isUser)
            return user;
        else
            return null;
    }

}
