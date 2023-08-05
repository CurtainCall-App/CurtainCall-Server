package org.cmc.curtaincall.domain.member.repository;

import org.cmc.curtaincall.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByNickname(String nickname);
}
