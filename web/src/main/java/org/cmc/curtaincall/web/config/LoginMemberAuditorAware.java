package org.cmc.curtaincall.web.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cmc.curtaincall.domain.account.repository.AccountRepository;
import org.cmc.curtaincall.domain.member.Member;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class LoginMemberAuditorAware implements AuditorAware<Member> {

    private final AccountRepository accountRepository;

    @Override
    public Optional<Member> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = (nonNull(authentication) && authentication.isAuthenticated())
//                ? authentication.getName() : null;
//        log.info("username={}", username);
//        Optional<Member> member = Optional.ofNullable(username)
//                .flatMap(accountRepository::findByUsernameAndUseYnIsTrue)
//                .map(Account::getMember)
//                .filter(Member::getUseYn);
//        log.info("Optional<Member>={}", member);
        return Optional.empty();
    }
}
