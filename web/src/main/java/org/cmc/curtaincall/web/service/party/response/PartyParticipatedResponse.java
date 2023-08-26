package org.cmc.curtaincall.web.service.party.response;

public record PartyParticipatedResponse(
        Long partyId,
        boolean participated
) {
}
