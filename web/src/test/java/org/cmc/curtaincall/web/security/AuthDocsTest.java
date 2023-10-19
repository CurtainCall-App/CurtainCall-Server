package org.cmc.curtaincall.web.security;

import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.common.RestDocsAttribute;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

@WebMvcTest(AuthDocsController.class)
class AuthDocsTest extends AbstractWebTest {

    @Test
    @WithMockUser
    void loginPage() throws Exception {
        mockMvc.perform(get("/oauth2/authorization/{provider}", "kakao"))
                .andDo(document("auth-login-page",
                        pathParameters(
                                parameterWithName("provider").description("OAuth2 Provider")
                                        .attributes(RestDocsAttribute.constraint("kakao"))
                        )
                ));
    }

}
