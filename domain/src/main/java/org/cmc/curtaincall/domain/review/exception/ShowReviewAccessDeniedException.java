package org.cmc.curtaincall.domain.review.exception;

import org.cmc.curtaincall.domain.core.AbstractDomainException;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.review.ShowReviewId;

public class ShowReviewAccessDeniedException extends AbstractDomainException {

    public ShowReviewAccessDeniedException(final ShowReviewId id, final MemberId memberId) {
        super(ReviewErrorCode.ACCESS_DENIED, "showReviewId=" + id + ", memberId=" + memberId);
    }

}
