package org.cmc.curtaincall.domain.show.exception;

import org.cmc.curtaincall.domain.common.DomainErrorCode;
import org.cmc.curtaincall.domain.common.DomainException;
import org.cmc.curtaincall.domain.show.Show;

public class ShowUnableToCancelReviewException extends DomainException {

    public ShowUnableToCancelReviewException(Show show) {
        super(DomainErrorCode.BAD_REQUEST, String.format(
                "show.id=%s, show.reviewCount=%s", show.getId(), show.getReviewCount()
        ));
    }

    @Override
    public String getExternalMessage() {
        return "리뷰를 삭제할 수 없음";
    }
}
