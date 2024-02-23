package org.cmc.curtaincall.domain.review.exception;

import org.cmc.curtaincall.domain.core.AbstractDomainException;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.review.ShowReviewId;
import org.cmc.curtaincall.domain.show.ShowId;

public class ShowReviewNotFoundException extends AbstractDomainException {

    public ShowReviewNotFoundException(final ShowReviewId id) {
        super(ReviewErrorCode.NOT_FOUND, "ShowReview.id=" + id);
    }

    public ShowReviewNotFoundException(final ShowId showId, final CreatorId creatorId) {
        super(ReviewErrorCode.NOT_FOUND, "ShowReview.showId=" + showId + ", createdBy=" + creatorId);
    }

}
