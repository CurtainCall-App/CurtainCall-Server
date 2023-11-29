package org.cmc.curtaincall.domain.party.exception;

import org.cmc.curtaincall.domain.core.AbstractDomainException;
import org.cmc.curtaincall.domain.party.PartyId;

public class PartyNotFoundException extends AbstractDomainException {

    public PartyNotFoundException(final PartyId partyId) {
        super(PartyErrorCode.NOT_FOUND, "PartyId=" + partyId);
    }

}
