package org.cmc.curtaincall.domain.party;

import org.cmc.curtaincall.domain.party.exception.PartyNotFoundException;
import org.cmc.curtaincall.domain.party.repository.PartyRepository;

public final class PartyHelper {

    private PartyHelper() {
        throw new UnsupportedOperationException();
    }

    public static Party get(PartyId id, PartyRepository partyRepository) {
        return partyRepository.findById(id.getId())
                .filter(Party::getUseYn)
                .orElseThrow(() -> new PartyNotFoundException(id));
    }

    public static Party getWithOptimisticLock(PartyId id, PartyRepository partyRepository) {
        return partyRepository.findWithLockById(id.getId())
                .filter(Party::getUseYn)
                .orElseThrow(() -> new PartyNotFoundException(id));
    }

}
