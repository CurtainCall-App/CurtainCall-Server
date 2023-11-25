package org.cmc.curtaincall.web.security;

import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.security.controller.LoginController;
import org.cmc.curtaincall.web.security.service.CurtainCallJwtEncoderService;
import org.cmc.curtaincall.web.security.service.UsernameService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
class LoginControllerDocsTest extends AbstractWebTest {

    @MockBean
    private UsernameService usernameService;

    @MockBean
    private CurtainCallJwtEncoderService jwtEncoderService;

    @Test
    @WithMockUser
    void login_Docs() throws Exception {
        // given
        given(usernameService.getUsername(any())).willReturn("test-username");
        Jwt jwt = mock(Jwt.class);
        given(jwt.getTokenValue()).willReturn("test-jwt");
        given(jwt.getExpiresAt()).willReturn(Instant.now());
        given(jwtEncoderService.encode("test-username")).willReturn(jwt);
        given(accountDao.getMemberId("test-username")).willReturn(new MemberId(123L));
        mockMvc.perform(post("/login")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").isNotEmpty())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.accessTokenExpiresAt").isNotEmpty())
                .andDo(document("security-login",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("OAuth2 Open ID 토큰 (id_token)")
                        ),
                        responseFields(
                                fieldWithPath("memberId").description("회원 ID"),
                                fieldWithPath("accessToken").description("커튼콜 액세스 토큰"),
                                fieldWithPath("accessTokenExpiresAt").description("커튼콜 액세스 토큰 만료 일시")
                        )
                ));
    }

}