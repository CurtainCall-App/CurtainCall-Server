package org.cmc.curtaincall.web.security;

import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.context.support.WithMockUser;

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

@WebMvcTest(AccessTokenController.class)
class AccessTokenControllerDocsTest extends AbstractWebTest {

    @MockBean
    private UsernameService usernameService;

    @MockBean
    private CurtainCallJwtEncoderService jwtEncoderService;

    @Test
    @WithMockUser
    void issueAccessToken_Docs() throws Exception {
        // given
        given(usernameService.getUsername(any())).willReturn("test-username");
        Jwt jwt = mock(Jwt.class);
        given(jwt.getTokenValue()).willReturn("test-jwt");
        given(jwtEncoderService.encode("test-username")).willReturn(jwt);

        mockMvc.perform(post("/v1/token")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("test-jwt"))
                .andDo(document("security-issue-access-token",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("OAuth2 Open ID 토큰 (id_token)")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").description("커튼콜 액세스 토큰")
                        )
                ));
    }

}