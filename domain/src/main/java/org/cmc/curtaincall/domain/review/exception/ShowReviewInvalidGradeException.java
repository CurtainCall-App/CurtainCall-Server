package org.cmc.curtaincall.domain.review.exception;

import org.cmc.curtaincall.domain.core.AbstractDomainException;

public class ShowReviewInvalidGradeException extends AbstractDomainException {

    public ShowReviewInvalidGradeException(final Integer grade) {
        super(ReviewErrorCode.INVALID_GRADE, "invalid ShowReview.grade=" + grade);
    }

}
