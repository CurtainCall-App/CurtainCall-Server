package org.cmc.curtaincall.domain.review.exception;

import org.cmc.curtaincall.domain.core.AbstractDomainException;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.show.ShowId;

public class ShowReviewAlreadyReviewedException extends AbstractDomainException {

    public ShowReviewAlreadyReviewedException(final ShowId showId, final CreatorId creatorId) {
        super(ReviewErrorCode.ALREADY_REVIEWED, "ShowId=" + showId + ", CreatorId=" + creatorId);
    }

}
