package org.cmc.curtaincall.domain.lostitem.exception;

import org.cmc.curtaincall.domain.core.AbstractDomainException;
import org.cmc.curtaincall.domain.lostitem.LostItemId;
import org.cmc.curtaincall.domain.member.MemberId;

public class LostItemAccessDeniedException extends AbstractDomainException {

    public LostItemAccessDeniedException(final LostItemId lostItemId, final MemberId memberId) {
        super(LostItemErrorCode.ACCESS_DENIED, "lostItemId" + lostItemId + ", memberId=" + memberId);
    }

}
