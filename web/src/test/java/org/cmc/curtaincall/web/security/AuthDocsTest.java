package org.cmc.curtaincall.web.security;

import org.cmc.curtaincall.web.common.RestDocsConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

@Import({RestDocsConfig.class})
@WebMvcTest(controllers = AuthDocsController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class) })
@AutoConfigureRestDocs
class AuthDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void loginResponse() throws Exception {
        mockMvc.perform(get("/docs/auth/login-response"))
                .andDo(document("auth-LoginResponse",
                        responseFields(
                                fieldWithPath("memberId").description("회원 ID"),
                                fieldWithPath("accessToken").description("엑세스 토큰"),
                                fieldWithPath("accessTokenExpiresAt").description("엑세스 토큰 만료 시간"),
                                fieldWithPath("refreshToken").description("리프레쉬 토큰"),
                                fieldWithPath("refreshTokenExpiresAt").description("리프레쉬 토큰 만료 시간")
                        )
                ));
    }

}
