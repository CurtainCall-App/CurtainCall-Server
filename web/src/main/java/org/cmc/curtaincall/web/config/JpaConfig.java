package org.cmc.curtaincall.web.config;

import org.cmc.curtaincall.domain.account.repository.AccountRepository;
import org.cmc.curtaincall.domain.member.Member;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class JpaConfig {

    @Bean
    public AuditorAware<Member> auditorProvider(AccountRepository accountRepository) {
        return new LoginMemberAuditorAware(accountRepository);
    }
}
