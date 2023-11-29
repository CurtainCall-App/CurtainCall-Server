package org.cmc.curtaincall.domain.party.exception;

import lombok.extern.slf4j.Slf4j;
import org.cmc.curtaincall.domain.core.AbstractDomainException;
import org.cmc.curtaincall.domain.party.PartyId;

@Slf4j
public class PartyAlreadyClosedException extends AbstractDomainException {

    public PartyAlreadyClosedException(final PartyId id) {
        super(PartyErrorCode.ALREADY_CLOSED, "PartyId=" + id);
    }

}
