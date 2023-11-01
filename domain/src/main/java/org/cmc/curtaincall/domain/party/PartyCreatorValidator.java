package org.cmc.curtaincall.domain.party;

import org.cmc.curtaincall.domain.core.CreatorId;

public interface PartyCreatorValidator {

    void validate(final PartyId partyId, final CreatorId creatorId);
}
