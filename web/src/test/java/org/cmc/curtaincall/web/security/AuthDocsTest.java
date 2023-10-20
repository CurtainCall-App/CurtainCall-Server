package org.cmc.curtaincall.web.security;

import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.common.RestDocsAttribute;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;

@WebMvcTest(AuthDocsController.class)
class AuthDocsTest extends AbstractWebTest {

    @Test
    void loginPage() throws Exception {
        mockMvc.perform(get("/oauth2/authorization/{provider}", "kakao"))
                .andDo(document("auth-login-page",
                        pathParameters(
                                parameterWithName("provider").description("OAuth2 Provider")
                                        .attributes(RestDocsAttribute.constraint("kakao"))
                        )
                ));
    }

    @Test
    void oauth2Login() throws Exception {
        mockMvc.perform(post("/login/oauth2/code/{provider}", "kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("code", "oidcToken")
                .queryParam("state", "oauth2State")
        ).andDo(document("auth-oauth2-login",
                pathParameters(
                        parameterWithName("provider").description("OAuth2 Provider. kakao, apple")
                ),
                queryParameters(
                        parameterWithName("code").description("OIDC Token"),
                        parameterWithName("state").description("OAuth2 state")
                ),
                responseFields(
                        fieldWithPath("accessToken").description("Curtain Call Token")
                )
        ));
    }

}
