package org.cmc.curtaincall.domain.review.exception;

import org.cmc.curtaincall.domain.core.AbstractDomainException;
import org.cmc.curtaincall.domain.show.ShowId;

public class ShowReviewStatsNotFoundException extends AbstractDomainException {

    public ShowReviewStatsNotFoundException(final ShowId id) {
        super(ReviewErrorCode.STATS_NOT_FOUND, "ShowId.id=" + id);
    }

}
