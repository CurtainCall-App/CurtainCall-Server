package org.cmc.curtaincall.domain.review.exception;

import org.cmc.curtaincall.domain.common.DomainErrorCode;
import org.cmc.curtaincall.domain.common.DomainException;
import org.cmc.curtaincall.domain.show.ShowId;

public class ShowReviewStatsNotFoundException extends DomainException {

    /**
     * ShowReviewStats 은 Show 가 생성되면서 같이 생성되어야 하므로 이것이 존재하지 않으면
     * INTERNAL_SERVER_ERROR
     */
    public ShowReviewStatsNotFoundException(ShowId id) {
        super(DomainErrorCode.INTERNAL_SERVER_ERROR, "ShowId.id=" + id);
    }

    @Override
    public String getExternalMessage() {
        return "리뷰통계가 존재하지 않습니다.";
    }
}
