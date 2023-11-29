package org.cmc.curtaincall.domain.party.exception;

import org.cmc.curtaincall.domain.core.AbstractDomainException;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.party.PartyId;

public class PartyAlreadyParticipatedException extends AbstractDomainException {

    public PartyAlreadyParticipatedException(final PartyId partyId, final MemberId memberId) {
        super(PartyErrorCode.ALREADY_PARTICIPATED, "partyId=" + partyId + ", memberId" + memberId);
    }

}
