package com.gachon.ufriendsserver.api.controller;

import com.gachon.ufriendsserver.api.common.ResponseCode;
import com.gachon.ufriendsserver.api.common.controller.CommonController;
import com.gachon.ufriendsserver.api.common.security.TokenProvider;
import com.gachon.ufriendsserver.api.domain.Member;
import com.gachon.ufriendsserver.api.dto.member.JoinDTO;
import com.gachon.ufriendsserver.api.dto.member.LoginDTO;
import com.gachon.ufriendsserver.api.dto.member.MemberDTO;
import com.gachon.ufriendsserver.api.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController extends CommonController {

    private final MemberService memberService;
    @Autowired
    private TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static PythonInterpreter interpreter;

    @GetMapping("/hello")
    public String hello(){
        return "Hello, U-Friends.";
    }

    @GetMapping("/pytest")
    public String pytest() {

//        System.setProperty("python.import.site", "false"); // jython-standalone이 아닐 경우 site 모듈 에러 해결 방안
        interpreter = new PythonInterpreter();
        // dev
//        interpreter.execfile("./src/main/python/kodial.py");
        interpreter.exec("print(chat('hello'))");
        // release
        interpreter.execfile("/var/lib/jenkins/workspace/ufriends-server/src/main/python/kodial.py");
        PyFunction pyFunction = interpreter.get("chat", PyFunction.class);

        String userInput = "hello";

        PyObject pyobj = pyFunction.__call__(new PyString(userInput));
        System.out.println(pyobj.toString());

        return pyobj.toString();
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

    // 전화번호 중복 확인
    @GetMapping("/phoneNumValid")
    public ResponseEntity<?> phoneNumValid(@RequestParam String phoneNum){
        String phoneNoUpdate = phoneNum.replaceAll("[^0-9]", "");
        if(memberService.isPhoneNumExisting(phoneNoUpdate))
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
    public ResponseEntity<?> login(@Validated @RequestBody LoginDTO loginDTO){
        Member member = memberService.getByCredentials(loginDTO.getEmail(), passwordEncoder.encode(loginDTO.getPassword()), passwordEncoder);

        if(member != null){
            final String token = tokenProvider.create(member);
            final MemberDTO memberDTO = MemberDTO.builder().email(member.getEmail()).email(member.getEmail()).token(token).build();
            return SuccessReturn(memberDTO);
        } else {
            return ErrorReturn(ResponseCode.LOGIN_ERROR);
        }

    }


    // 비밀번호 찾기



}
