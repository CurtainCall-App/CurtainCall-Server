package org.cmc.curtaincall.domain.party.repository;

import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.party.Party;
import org.cmc.curtaincall.domain.party.PartyMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PartyMemberRepository extends JpaRepository<PartyMember, Long> {

    long countByMemberId(MemberId memberId);

    List<PartyMember> findAllByMemberIdAndPartyIn(MemberId memberId, Collection<Party> parties);
}
