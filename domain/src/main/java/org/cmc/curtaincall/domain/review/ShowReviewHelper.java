package org.cmc.curtaincall.domain.review;

import org.cmc.curtaincall.domain.review.exception.ShowReviewNotFoundException;
import org.cmc.curtaincall.domain.review.repository.ShowReviewRepository;

public final class ShowReviewHelper {

    private ShowReviewHelper() {
        throw new UnsupportedOperationException();
    }

    public static ShowReview get(ShowReviewId id, ShowReviewRepository showReviewRepository) {
        return showReviewRepository.findById(id.getId())
                .filter(ShowReview::getUseYn)
                .orElseThrow(() -> new ShowReviewNotFoundException(id));
    }

    public static ShowReview getWithOptimisticLock(ShowReviewId id, ShowReviewRepository showReviewRepository) {
        return showReviewRepository.findWithLockById(id.getId())
                .filter(ShowReview::getUseYn)
                .orElseThrow(() -> new ShowReviewNotFoundException(id));
    }

}
