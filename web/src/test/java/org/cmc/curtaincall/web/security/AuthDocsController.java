package org.cmc.curtaincall.web.security;

import org.cmc.curtaincall.web.security.response.LoginResponse;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthDocsController {

    @GetMapping("/oauth2/authorization/{provider}")
    public String loginPage(@PathVariable String provider) {
        return "loginPage";
    }

    @PostMapping("/login/oauth2/code/{provider}")
    public LoginResponse oauth2Login(
            @PathVariable String provider, @RequestParam String code, @RequestParam String state
    ) {
        return new LoginResponse("ACCESS_TOKEN");
    }
}
