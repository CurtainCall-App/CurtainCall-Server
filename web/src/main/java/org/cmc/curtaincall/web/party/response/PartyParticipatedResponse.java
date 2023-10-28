package org.cmc.curtaincall.web.party.response;

public record PartyParticipatedResponse(
        Long partyId,
        boolean participated
) {
}
