package org.cmc.curtaincall.web.security;

import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

@WebMvcTest(AuthDocsController.class)
class AuthDocsTest extends AbstractWebTest {

    @Test
    @WithMockUser
    void loginResponse() throws Exception {
        mockMvc.perform(get("/docs/auth/login-response"))
                .andDo(document("auth-LoginResponse",
                        responseFields(
                                fieldWithPath("accessToken").description("엑세스 토큰")
                        )
                ));
    }

}
