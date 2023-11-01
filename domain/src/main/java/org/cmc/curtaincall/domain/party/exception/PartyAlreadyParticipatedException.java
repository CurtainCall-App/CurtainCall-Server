package org.cmc.curtaincall.domain.party.exception;

import org.cmc.curtaincall.domain.common.DomainErrorCode;
import org.cmc.curtaincall.domain.common.DomainException;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.party.PartyId;

public class PartyAlreadyParticipatedException extends DomainException {

    public PartyAlreadyParticipatedException(final PartyId partyId, final MemberId memberId) {
        super(DomainErrorCode.BAD_REQUEST, partyId + ", " + memberId);
    }

    @Override
    public String getExternalMessage() {
        return "이미 참여한 파티입니다.";
    }
}
