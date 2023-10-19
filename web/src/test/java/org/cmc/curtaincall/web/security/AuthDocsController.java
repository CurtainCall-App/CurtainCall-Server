package org.cmc.curtaincall.web.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

// http://localhost:8080/login/oauth2/code/kakao?code=mFYutND-rel570vmGbigS1M0uPvIDOxE90ciLy9vDSay12a68zvwfyVKuIEKKw0gAAABi0gbpH6xu3fh8M0xkQ&state=j_wyQVld38JBofTL3PIRwZwyg7nP4F_m3GFoENzmu0U%3D
@RestController
public class AuthDocsController {

    @GetMapping("/oauth2/authorization/{provider}")
    public String loginPage(@PathVariable String provider) {
        return "loginPage";
    }

}
