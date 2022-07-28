package com.gachon.ufriendsserver;

import com.gachon.ufriendsserver.api.dto.JoinDTO;
import com.gachon.ufriendsserver.api.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.gachon.ufriendsserver.ApiDocumentUtils.getDocumentRequest;
import static com.gachon.ufriendsserver.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "ufriends.com")
@Transactional
public class MemberControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RestDocumentationContextProvider restDocumentationContextProvider;

    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    public DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    public String nickname = "tester";
    public String email = "tester@email.com";
    public String password = "qwerty1234";
    public String phoneNum = "010-1234-5678";
    public LocalDate birthday = LocalDate.parse("11/27/2020", inputFormat);

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        JoinDTO joinDTO = JoinDTO.builder()
                .nickname(nickname)
                .email(email)
                .password(password)
                .phoneNum(phoneNum)
                .birthday(birthday)
                .build();

        memberService.join(joinDTO);
    }

    @Test
    public void join() throws Exception {

        String content = "{" +
                "\"nickname\":\"test\"," +
                "\"email\":\"test@email.com\"," +
                "\"password\":\"qwerty1234\"," +
                "\"phoneNum\":\"010-0000-0000\"," +
                "\"birthday\":\"2001-01-01\"" +
                "}";

        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders
                        .post("/api/member/join")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(
                        document("join"
                                , getDocumentRequest()
                                , getDocumentResponse()
                                , requestFields (
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임")
                                        , fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                                        , fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                        , fieldWithPath("phoneNum").type(JsonFieldType.STRING).description("전화번호")
                                        , fieldWithPath("birthday").type(JsonFieldType.STRING).description("생년월일")
                                )
                                , responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태코드")
                                        , fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터")
                                ).andWithPrefix("data.",
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("멤버 고유 숫자")
                                        , fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임")
                                        , fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                                        , fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호(보안 상 \"\")")
                                        , fieldWithPath("phoneNum").type(JsonFieldType.STRING).description("전화번호")
                                        , fieldWithPath("birthday").type(JsonFieldType.STRING).description("생년월일")
                                        , fieldWithPath("joinDate").type(JsonFieldType.STRING).description("가입일자")
                                        )
                        ))
                .andDo(print());

    }

    @Test
    public void emailValid() throws Exception {

        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders
                        .get("/api/member/emailValid?email={email}", email)
        );

        result.andExpect(status().isOk())
                .andDo(
                        document("emailValid"
                                , getDocumentResponse()
                                , responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태코드")
                                        , fieldWithPath("data").type(JsonFieldType.NULL).description("데이터")
                                )
                        ))
                .andDo(print());
    }

    @Test
    public void login() throws Exception {

        String content = "{" +
                "\"email\":\"tester@email.com\"," +
                "\"password\":\"qwerty1234\"" +
                "}";

        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders
                        .post("/api/member/login")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(
                        document("login"
                                , getDocumentRequest()
                                , getDocumentResponse()
                                , requestFields (
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                                        , fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                )
                                , responseFields(
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태코드")
                                        , fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터")
                                ).andWithPrefix("data.",
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("멤버 고유 숫자")
                                        , fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임")
                                        , fieldWithPath("email").type(JsonFieldType.STRING).description("이메일")
                                        , fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호(보안 상 \"\")")
                                        , fieldWithPath("phoneNum").type(JsonFieldType.STRING).description("전화번호")
                                        , fieldWithPath("birthday").type(JsonFieldType.STRING).description("생년월일")
                                        , fieldWithPath("joinDate").type(JsonFieldType.STRING).description("가입일자")
                                )
                        ))
                .andDo(print());

    }

}
