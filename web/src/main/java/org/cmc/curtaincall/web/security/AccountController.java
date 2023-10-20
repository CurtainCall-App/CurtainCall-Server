package org.cmc.curtaincall.web.security;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.account.dao.AccountDao;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.web.common.response.IdResult;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountDao accountDao;

    @GetMapping("/user/member-id")
    public IdResult<Long> getUserMemberId(Authentication authentication) {
        String username = authentication.getName();
        return new IdResult<>(accountDao.findMemberIdByUsername(username).map(MemberId::getId).orElse(null));
    }
}
