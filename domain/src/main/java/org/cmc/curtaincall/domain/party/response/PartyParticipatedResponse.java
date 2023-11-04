package org.cmc.curtaincall.domain.party.response;

import org.cmc.curtaincall.domain.party.PartyId;

public record PartyParticipatedResponse(
        PartyId partyId,
        boolean participated
) {
}
