package org.cmc.curtaincall.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cmc.curtaincall.web.common.RestDocsConfig;
import org.cmc.curtaincall.web.service.account.AccountService;
import org.cmc.curtaincall.web.service.common.response.BooleanResult;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.member.MemberService;
import org.cmc.curtaincall.web.service.member.request.MemberCreate;
import org.cmc.curtaincall.web.service.member.response.MemberDetailResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import(RestDocsConfig.class)
@AutoConfigureRestDocs
@WebMvcTest(MemberController.class)
class MemberControllerDocsTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MemberService memberService;

    @MockBean
    AccountService accountService;

    @Test
    @WithMockUser
    void getNicknameDuplicate_Docs() throws Exception {
        // given
        BooleanResult duplicateResult = new BooleanResult(false);
        given(memberService.checkNicknameDuplicate("테스트닉네임")).willReturn(duplicateResult);

        // expected
        mockMvc.perform(get("/members/duplicate/nickname")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .param("nickname", "테스트닉네임"))
                .andDo(print())
                .andDo(document("member-get-nickname-duplicate",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        queryParameters(
                                parameterWithName("nickname").description("중복 확인 하려는 닉네임. 최대 15.")
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
        MemberCreate memberCreate = MemberCreate.builder()
                .nickname("연뮤더쿠")
                .build();

        given(memberService.create(any())).willReturn(new IdResult<>(1L));

        // expected
        mockMvc.perform(post("/signup").with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberCreate))
                )
                .andDo(print())
                .andDo(document("member-signup",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        requestFields(
                                fieldWithPath("nickname").description("회원 닉네임")
                        ),
                        responseFields(
                                fieldWithPath("id").description("생성된 회원 ID (memberId)")
                        )
                ))
        ;
        then(accountService).should().signupMember(anyString(), eq(1L));
    }

    @Test
    @WithMockUser
    void getMemberDetail_Docs() throws Exception {
        // given
        MemberDetailResponse response = MemberDetailResponse.builder()
                .id(10L)
                .nickname("고라파덕")
                .imageUrl(null)
                .recruitingNum(5L)
                .participationNum(10L)
                .build();
        given(memberService.getDetail(any())).willReturn(response);

        // expected
        mockMvc.perform(get("/members/{memberId}", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-get-member-detail",
                        pathParameters(
                                parameterWithName("memberId").description("회원 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("회원 ID"),
                                fieldWithPath("nickname").description("닉네임"),
                                fieldWithPath("imageUrl").description("회원 이미지, 없을 경우 NULL"),
                                fieldWithPath("recruitingNum").description("My 모집"),
                                fieldWithPath("participationNum").description("My 참여")
                        )
                ))
        ;

    }
}