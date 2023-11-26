package org.cmc.curtaincall.web.security;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthDocsController {

    @GetMapping("/oauth2/authorization/{provider}")
    @WithMockUser
    public String loginPage(@PathVariable String provider) {
        return provider + "-loginPage";
    }

}
