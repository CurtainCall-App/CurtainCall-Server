package org.cmc.curtaincall.domain.party.repository;

import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.party.Party;
import org.cmc.curtaincall.domain.party.PartyMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyMemberRepository extends JpaRepository<PartyMember, Long> {

    long countByMemberId(MemberId memberId);

    long deleteAllByParty(Party party);

}
