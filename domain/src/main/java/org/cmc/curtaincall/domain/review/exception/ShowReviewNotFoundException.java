package org.cmc.curtaincall.domain.review.exception;

import org.cmc.curtaincall.domain.core.AbstractDomainException;
import org.cmc.curtaincall.domain.review.ShowReviewId;

public class ShowReviewNotFoundException extends AbstractDomainException {

    public ShowReviewNotFoundException(final ShowReviewId id) {
        super(ReviewErrorCode.NOT_FOUND, "ShowReview.id=" + id);
    }

}
