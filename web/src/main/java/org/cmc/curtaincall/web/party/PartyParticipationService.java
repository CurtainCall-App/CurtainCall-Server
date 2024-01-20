package org.cmc.curtaincall.web.party;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.OptimisticLock;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.party.Party;
import org.cmc.curtaincall.domain.party.PartyHelper;
import org.cmc.curtaincall.domain.party.PartyId;
import org.cmc.curtaincall.domain.party.repository.PartyRepository;
import org.cmc.curtaincall.domain.party.validation.PartyMemberIdValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartyParticipationService {

    private final PartyRepository partyRepository;

    private final PartyMemberIdValidator memberIdValidator;

    @Transactional
    @OptimisticLock
    public void participate(final PartyId partyId, final MemberId memberId) {
        memberIdValidator.validate(memberId);
        Party party = PartyHelper.getWithOptimisticLock(partyId, partyRepository);
        party.participate(memberId);
    }

    @Transactional
    @OptimisticLock
    public void leave(final PartyId partyId, final MemberId memberId) {
        Party party = PartyHelper.getWithOptimisticLock(partyId, partyRepository);
        party.leave(memberId);
    }
}
