package org.cmc.curtaincall.web.security;

import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.security.controller.AccountController;
import org.cmc.curtaincall.web.security.service.CurtainCallJwtEncoderService;
import org.cmc.curtaincall.web.security.service.SignupService;
import org.cmc.curtaincall.web.security.service.UsernameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest extends AbstractWebTest {

    @Autowired
    private JwtDecoder jwtDecoder;

    @MockBean
    private SignupService signupService;

    @MockBean
    private UsernameService usernameService;

    @MockBean
    private CurtainCallJwtEncoderService jwtEncoderService;

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
                .andExpect(jsonPath("$.id").value(AbstractWebTest.LOGIN_MEMBER_ID.getId()))
        ;
    }

    @Test
    void getUserMemberId_NotSignup() throws Exception {
        // given
        String token = "ACCESS_TOKEN";
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusMillis(1000 * 60 * 60);
        Jwt jwt = Jwt.withTokenValue(token)
                .header("alg", MacAlgorithm.HS256.getName())
                .subject("not-signup")
                .issuer("curtaincall")
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .build();
        given(jwtDecoder.decode("NOT_SIGNUP_ACCESS_TOKEN")).willReturn(jwt);
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