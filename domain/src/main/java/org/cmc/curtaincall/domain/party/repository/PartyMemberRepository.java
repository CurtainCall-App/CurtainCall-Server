package org.cmc.curtaincall.domain.party.repository;

import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.party.PartyMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyMemberRepository extends JpaRepository<PartyMember, Long> {

    long countByMember(Member member);

    Slice<PartyMember> findSliceByMemberOrderByPartyDesc(Pageable pageable, Member member);
}
