package org.cmc.curtaincall.domain.party.exception;

import lombok.extern.slf4j.Slf4j;
import org.cmc.curtaincall.domain.core.AbstractDomainException;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.party.PartyId;

@Slf4j
public class PartyRecruiterNotAllowedLeaveException extends AbstractDomainException {

    public PartyRecruiterNotAllowedLeaveException(final PartyId id, final MemberId memberId) {
        super(PartyErrorCode.NOT_PARTICIPATED, "PartyId=" + id + ", MemberId=" + memberId);
    }

}
