package org.cmc.curtaincall.domain.member.repository;

import org.cmc.curtaincall.domain.member.MemberWithdrawal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberWithdrawalRepository extends JpaRepository<MemberWithdrawal, Long> {
}
