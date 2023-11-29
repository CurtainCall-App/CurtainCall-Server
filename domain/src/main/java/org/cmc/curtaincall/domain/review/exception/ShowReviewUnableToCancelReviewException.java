package org.cmc.curtaincall.domain.review.exception;

import org.cmc.curtaincall.domain.core.DomainErrorCode;
import org.cmc.curtaincall.domain.core.DomainException;
import org.cmc.curtaincall.domain.show.ShowId;

public class ShowReviewUnableToCancelReviewException extends DomainException {

    public ShowReviewUnableToCancelReviewException(ShowId id) {
        super(DomainErrorCode.BAD_REQUEST, "id=" + id);
    }

    @Override
    public String getExternalMessage() {
        return "리뷰를 삭제할 수 없음";
    }
}
