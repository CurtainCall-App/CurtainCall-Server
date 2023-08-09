package org.cmc.curtaincall.web.config;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.account.Account;
import org.cmc.curtaincall.domain.account.repository.AccountRepository;
import org.cmc.curtaincall.domain.member.Member;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
public class LoginMemberAuditorAware implements AuditorAware<Member> {

    private final AccountRepository accountRepository;

    @Override
    public Optional<Member> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (nonNull(authentication) && authentication.isAuthenticated())
                ? authentication.getName() : null;
        return Optional.ofNullable(username)
                .flatMap(accountRepository::findByUsernameAndUseYnIsTrue)
                .map(Account::getMember)
                .filter(Member::getUseYn);
    }
}
