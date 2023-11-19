package org.cmc.curtaincall.domain.account.dao;

import org.assertj.core.api.Assertions;
import org.cmc.curtaincall.domain.account.Account;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.account.repository.AccountRepository;
import org.cmc.curtaincall.domain.common.AbstractDataJpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(AccountDao.class)
class AccountDaoTest extends AbstractDataJpaTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountDao accountDao;

    @Test
    void findMemberIdByUsername_Success() {
        // given
        Account account = Account.builder()
                .username("test-username")
                .memberId(new MemberId(10L))
                .build();
        accountRepository.save(account);

        em.flush();
        em.clear();

        // expected
        Assertions.assertThat(accountDao.findMemberIdByUsername("test-username"))
                .isNotEmpty()
                .contains(new MemberId(10L));
    }

}