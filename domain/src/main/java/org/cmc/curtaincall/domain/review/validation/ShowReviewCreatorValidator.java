package org.cmc.curtaincall.domain.review.validation;

import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.review.ShowReviewId;

public interface ShowReviewCreatorValidator {

    void validate(final ShowReviewId id, final CreatorId creatorId);
}
