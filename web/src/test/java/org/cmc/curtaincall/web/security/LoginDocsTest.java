package org.cmc.curtaincall.web.security;

import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.security.request.TokenLoginRequest;
import org.cmc.curtaincall.web.security.response.LoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(LoginDocsTest.TestLoginController.class)
@Import(LoginDocsTest.TestLoginController.class)
class LoginDocsTest extends AbstractWebTest {

    @Test
    void tokenLogin() throws Exception {
        // expected
        mockMvc.perform(post("/login/oauth2/token/{provider}", "kakao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TokenLoginRequest("SOCIAL_LOGIN_TOKEN")))
                )
                .andDo(print())
                .andDo(document("security-token-login",
                        pathParameters(
                                parameterWithName("provider").description("소셜 로그인 제공자. apple, kakao, naver")
                        ),
                        requestFields(
                                fieldWithPath("token").description("소셜로그인 토큰. naver 는 Access token. kakao, apple 은 ID token")
                        ),
                        responseFields(
                                fieldWithPath("memberId").optional().description("회원 ID. 회원가입 안되어 있을 경우 null"),
                                fieldWithPath("accessToken").description("커튼콜 액세스 토큰. 소셜 로그인 토큰 X"),
                                fieldWithPath("accessTokenExpiresAt").description("커튼콜 액세스 토큰 만료일시")
                        )
                ))
        ;
    }

    @RestController
    static class TestLoginController {

        @PostMapping("/login/oauth2/token/{provider}")
        public LoginResponse login(
                @RequestBody final TokenLoginRequest request, @PathVariable final String provider
        ) {
            return new LoginResponse(LOGIN_MEMBER_ID.getId(), "ACCESS_TOKEN", LocalDateTime.of(2024, 1, 14, 14, 41));
        }
    }
}
