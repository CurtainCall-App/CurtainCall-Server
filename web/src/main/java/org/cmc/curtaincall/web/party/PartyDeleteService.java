package org.cmc.curtaincall.web.party;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.party.Party;
import org.cmc.curtaincall.domain.party.PartyId;
import org.cmc.curtaincall.domain.party.exception.PartyNotFoundException;
import org.cmc.curtaincall.domain.party.repository.PartyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartyDeleteService {

    private final PartyRepository partyRepository;

    @Transactional
    public void delete(final PartyId partyId) {
        final Party party = partyRepository.findWithPessimisticLockById(partyId.getId())
                .orElseThrow(() -> new PartyNotFoundException(partyId));
        party.getPartyMembers()
                .forEach(partyMember -> party.cancelParticipate(partyMember.getMemberId()));
        partyRepository.delete(party);
    }
}
