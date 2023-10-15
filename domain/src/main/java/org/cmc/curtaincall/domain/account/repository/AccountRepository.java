package org.cmc.curtaincall.domain.account.repository;

import org.cmc.curtaincall.domain.account.Account;
import org.cmc.curtaincall.domain.account.MemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsernameAndUseYnIsTrue(String username);

    Optional<Account> findByMemberId(MemberId memberId);
}
