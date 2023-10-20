package org.cmc.curtaincall.domain.account;

import org.cmc.curtaincall.domain.account.repository.AccountRepository;
import org.cmc.curtaincall.domain.common.AbstractDataJpaTest;
import org.cmc.curtaincall.domain.member.MemberId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest extends AbstractDataJpaTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void memberId_NotNull() {
        // given
        Account account = new Account("test-username");
        account.registerMember(new MemberId(1L));

        // when
        accountRepository.save(account);
        em.flush();
        em.clear();

        // then
        Account result = accountRepository.findById(account.getId()).orElseThrow();
        assertThat(result.getMemberId()).isEqualTo(new MemberId(1L));
    }

    @Test
    void memberId_Null() {
        // given
        Account account = new Account("test-username");

        // when
        accountRepository.save(account);
        em.flush();
        em.clear();

        // then
        Account result = accountRepository.findById(account.getId()).orElseThrow();
        assertThat(result.getMemberId()).isNull();
    }
}