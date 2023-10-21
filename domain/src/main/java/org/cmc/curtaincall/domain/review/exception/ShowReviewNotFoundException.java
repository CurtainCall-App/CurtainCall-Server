package org.cmc.curtaincall.domain.review.exception;

import org.cmc.curtaincall.domain.common.DomainErrorCode;
import org.cmc.curtaincall.domain.common.DomainException;

public class ShowReviewNotFoundException extends DomainException {

    public ShowReviewNotFoundException(Long id) {
        super(DomainErrorCode.NOT_FOUND, "ShowReview.id=" + id);
    }

    @Override
    public String getExternalMessage() {
        return "리뷰가 존재하지 않습니다.";
    }
}
