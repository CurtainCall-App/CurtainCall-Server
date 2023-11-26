package org.cmc.curtaincall.domain.review;

import org.cmc.curtaincall.domain.review.exception.ShowReviewStatsNotFoundException;
import org.cmc.curtaincall.domain.review.repository.ShowReviewStatsRepository;
import org.cmc.curtaincall.domain.show.ShowId;

public final class ShowReviewStatsHelper {

    private ShowReviewStatsHelper() {
        throw new UnsupportedOperationException();
    }

    public static ShowReviewStats getWithOptimisticLock(ShowId id, ShowReviewStatsRepository showReviewStatsRepository) {
        return showReviewStatsRepository.findWithLockById(id)
                .filter(ShowReviewStats::getUseYn)
                .orElseThrow(() -> new ShowReviewStatsNotFoundException(id));
    }

}
