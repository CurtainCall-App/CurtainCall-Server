package org.cmc.curtaincall.web.security;

import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest extends AbstractWebTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void getUserMemberId_Signup() throws Exception {
        // expected
        mockMvc.perform(get("/user/member-id")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(loginMemberId))
        ;
    }

    @Test
    void getUserMemberId_NotSignup() throws Exception {
        // given
        given(jwtTokenProvider.getSubject("NOT_SIGNUP_ACCESS_TOKEN"))
                .willReturn("not-signup");
        given(jwtTokenProvider.validateToken("NOT_SIGNUP_ACCESS_TOKEN")).willReturn(true);
        given(accountDao.findMemberIdByUsername("not-signup")).willReturn(Optional.empty());

        // expected
        mockMvc.perform(get("/user/member-id")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer NOT_SIGNUP_ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isEmpty())
        ;
    }
}