package org.cmc.curtaincall.domain.party.exception;

import lombok.extern.slf4j.Slf4j;
import org.cmc.curtaincall.domain.core.DomainErrorCode;
import org.cmc.curtaincall.domain.core.DomainException;
import org.cmc.curtaincall.domain.party.PartyId;

@Slf4j
public class PartyAlreadyClosedException extends DomainException {

    public PartyAlreadyClosedException(PartyId id) {
        super(DomainErrorCode.BAD_REQUEST, "PartyId=" + id);
    }

    @Override
    public String getExternalMessage() {
        return "마감된 파티입니다.";
    }
}
