package org.cmc.curtaincall.web.security;

import org.cmc.curtaincall.web.security.response.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequestMapping("/docs/auth")
@RestController
public class AuthDocsController {

    @GetMapping("/login-response")
    public ResponseEntity<LoginResponse> loginResponse() {
        LoginResponse loginResponse = LoginResponse.builder()
                .memberId(1L)
                .accessToken("<Access token>")
                .accessTokenExpiresAt(LocalDateTime.of(2023, 8, 26, 1, 53))
                .refreshToken("<Refresh token>")
                .refreshTokenExpiresAt(LocalDateTime.of(2023, 9, 26, 1, 53))
                .build();
        return ResponseEntity.ok(loginResponse);
    }

}
