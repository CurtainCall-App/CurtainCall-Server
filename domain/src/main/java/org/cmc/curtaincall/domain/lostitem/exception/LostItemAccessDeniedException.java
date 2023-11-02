package org.cmc.curtaincall.domain.lostitem.exception;

import org.cmc.curtaincall.domain.common.DomainErrorCode;
import org.cmc.curtaincall.domain.common.DomainException;
import org.cmc.curtaincall.domain.lostitem.LostItemId;
import org.cmc.curtaincall.domain.member.MemberId;

public class LostItemAccessDeniedException extends DomainException {

    public LostItemAccessDeniedException(final LostItemId lostItemId, final MemberId memberId, final String message) {
        super(DomainErrorCode.FORBIDDEN, message + " " + lostItemId + ", " + memberId);
    }

    @Override
    public String getExternalMessage() {
        return getMessage();
    }
}
