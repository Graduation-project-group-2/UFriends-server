package com.gachon.ufriendsserver.api.service;

import com.gachon.ufriendsserver.api.domain.User;
import com.gachon.ufriendsserver.api.dto.user.JoinDTO;
import com.gachon.ufriendsserver.api.dto.user.LoginDTO;
import com.gachon.ufriendsserver.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return userRepository.save(User.builder()
                .email(joinDTO.getEmail())
                .nickname(joinDTO.getNickname())
                .password(passwordEncoder.encode(joinDTO.getPassword()))
                .build());
    }

    public User login(LoginDTO loginDTO){
        User originalUser = userRepository.findByEmail(loginDTO.getEmail()).orElseThrow(() -> new NullPointerException("Not Found User"));
        if(originalUser != null && passwordEncoder.matches(loginDTO.getPassword(), originalUser.getPassword())){
            return originalUser;
        }
        return null;
    }

    // 비밀번호 변경
    public void changePassword(String checkedEmail, String newPassword) {
        User user = userRepository.findByEmail(checkedEmail).orElseThrow(() -> new IllegalArgumentException("The " + checkedEmail +
                "email does not exist"));
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    public User getUserById(Integer userId){
        return userRepository.findByUserId(userId).orElseThrow(() -> new NullPointerException("NOT FOUND USER"));
    }

    // 회원탈퇴
    public void deleteUser(int userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new NullPointerException("NOT FOUND USER"));
        userRepository.delete(user);
    }


}
