package org.cmc.curtaincall.domain.account.repository;

import org.cmc.curtaincall.domain.account.Account;
import org.cmc.curtaincall.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsernameAndUseYnIsTrue(String username);

    Optional<Account> findByMember(Member member);
}
