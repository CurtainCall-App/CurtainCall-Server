package org.cmc.curtaincall.domain.review.exception;

import org.cmc.curtaincall.domain.common.DomainErrorCode;
import org.cmc.curtaincall.domain.common.DomainException;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.review.ShowReviewId;

public class ShowReviewAccessDeniedException extends DomainException {

    public ShowReviewAccessDeniedException(final ShowReviewId id, final MemberId memberId, final String message) {
        super(DomainErrorCode.FORBIDDEN, message + " " + id + ", " + memberId);
    }

    @Override
    public String getExternalMessage() {
        return getMessage();
    }
}
