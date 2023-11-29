package org.cmc.curtaincall.domain.party.exception;

import org.cmc.curtaincall.domain.core.AbstractDomainException;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.party.PartyId;

public class PartyAccessDeniedException extends AbstractDomainException {

    public PartyAccessDeniedException(final PartyId partyId, final MemberId memberId) {
        super(PartyErrorCode.ACCESS_DENIED, "partyId=" + partyId + ", memberId=" + memberId);
    }

}
