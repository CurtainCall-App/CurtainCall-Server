package org.cmc.curtaincall.domain.party.validation;

import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.party.PartyId;

public interface PartyCreatorValidator {

    void validate(final PartyId partyId, final CreatorId creatorId);
}
