package org.cmc.curtaincall.domain.party.exception;

import org.cmc.curtaincall.domain.common.DomainErrorCode;
import org.cmc.curtaincall.domain.common.DomainException;
import org.cmc.curtaincall.domain.party.PartyId;

public class PartyNotFoundException extends DomainException {

    public PartyNotFoundException(PartyId partyId) {
        super(DomainErrorCode.NOT_FOUND, "PartyId=" + partyId);
    }

    @Override
    public String getExternalMessage() {
        return "존재하지 않는 파티 입니다.";
    }
}
