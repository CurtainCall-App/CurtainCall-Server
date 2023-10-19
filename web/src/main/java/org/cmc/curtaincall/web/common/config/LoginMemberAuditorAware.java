package org.cmc.curtaincall.web.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.account.dao.AccountDao;
import org.cmc.curtaincall.domain.member.Member;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class LoginMemberAuditorAware implements AuditorAware<Member> {

    private final AccountDao accountDao;

    @Override
    public Optional<Member> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getName)
                .flatMap(accountDao::findMemberIdByUsername)
                .map(MemberId::getId)
                .map(Member::new);
    }
}
