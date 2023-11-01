package org.cmc.curtaincall.domain.party.response;

public record PartyParticipatedResponse(
        long partyId,
        boolean participated
) {
}
