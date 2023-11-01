package org.cmc.curtaincall.domain.party.exception;

import org.cmc.curtaincall.domain.common.DomainErrorCode;
import org.cmc.curtaincall.domain.common.DomainException;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.party.PartyId;

public class PartyAccessDeniedException extends DomainException {

    public PartyAccessDeniedException(final PartyId partyId, final MemberId memberId, final String message) {
        super(DomainErrorCode.FORBIDDEN, message + " " + partyId + ", " + memberId);
    }

    @Override
    public String getExternalMessage() {
        return getMessage();
    }
}
