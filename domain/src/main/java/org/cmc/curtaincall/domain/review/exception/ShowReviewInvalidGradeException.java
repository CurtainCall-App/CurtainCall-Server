package org.cmc.curtaincall.domain.review.exception;

import org.cmc.curtaincall.domain.core.DomainErrorCode;
import org.cmc.curtaincall.domain.core.DomainException;

public class ShowReviewInvalidGradeException extends DomainException {

    public ShowReviewInvalidGradeException(Integer grade) {
        super(DomainErrorCode.BAD_REQUEST, "invalid ShowReview.grade=" + grade);
    }

    @Override
    public String getExternalMessage() {
        return "리뷰 별점은 0~5 의 값";
    }
}
