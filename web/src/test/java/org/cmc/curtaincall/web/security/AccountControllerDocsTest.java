package org.cmc.curtaincall.web.security;

import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.common.RestDocsAttribute;
import org.cmc.curtaincall.web.security.controller.AccountController;
import org.cmc.curtaincall.web.security.request.SignupRequest;
import org.cmc.curtaincall.web.security.service.CurtainCallJwtEncoderService;
import org.cmc.curtaincall.web.security.service.SignupService;
import org.cmc.curtaincall.web.security.service.UsernameService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerDocsTest extends AbstractWebTest {

    @MockBean
    private SignupService signupService;

    @MockBean
    private UsernameService usernameService;

    @MockBean
    private CurtainCallJwtEncoderService jwtEncoderService;

    @Test
    void getUserMemberId() throws Exception {
        // given
        given(accountDao.findMemberIdByUsername(any())).willReturn(Optional.of(new MemberId(1L)));

        // expected
        mockMvc.perform(get("/user/member-id")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("account-get-user-member",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        responseFields(
                                fieldWithPath("id").description("로그인된 유저 회원 ID")
                        )
                ))
        ;
    }

    @Test
    void getNicknameDuplicate_Docs() throws Exception {
        // given
        given(signupService.checkNicknameDuplicate("테스트닉네임")).willReturn(false);

        // expected
        mockMvc.perform(get("/members/duplicate/nickname")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .param("nickname", "테스트닉네임"))
                .andDo(print())
                .andDo(document("account-get-nickname-duplicate",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        queryParameters(
                                parameterWithName("nickname").description("중복 확인 하려는 닉네임.")
                        ),
                        responseFields(
                                fieldWithPath("result").description("중복 여부. 중복이면 true.")
                        )
                ))
        ;
    }

    @Test
    @WithMockUser
    void signup_Docs() throws Exception {
        // given
        var request = SignupRequest.builder()
                .nickname("연뮤더쿠znzn")
                .build();

        given(signupService.signup(any(), any())).willReturn(new MemberId(123L));

        // expected
        mockMvc.perform(post("/signup")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andDo(document("account-signup",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        requestFields(
                                fieldWithPath("nickname").description("회원 닉네임")
                                        .attributes(RestDocsAttribute.constraint("min = 2, max = 15"))
                        ),
                        responseFields(
                                fieldWithPath("id").description("회원 ID")
                        )
                ))
        ;
    }
}