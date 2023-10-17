package org.cmc.curtaincall.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {

    @GetMapping("/user")
    public Authentication getUser(Authentication authentication) {

        return authentication;
    }
}
