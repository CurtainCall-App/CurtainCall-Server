package org.cmc.curtaincall.domain.review.infra;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.OptimisticLock;
import org.cmc.curtaincall.domain.review.ShowReview;
import org.cmc.curtaincall.domain.review.ShowReviewGradeApplyService;
import org.cmc.curtaincall.domain.review.ShowReviewStats;
import org.cmc.curtaincall.domain.review.ShowReviewStatsHelper;
import org.cmc.curtaincall.domain.review.repository.ShowReviewStatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShowReviewGradeApplyServiceImpl implements ShowReviewGradeApplyService {

    private final ShowReviewStatsRepository showReviewStatsRepository;

    @Transactional
    @OptimisticLock
    @Override
    public void apply(final ShowReview showReview) {
        final ShowReviewStats reviewStats = ShowReviewStatsHelper.getWithOptimisticLock(
                showReview.getShowId(), showReviewStatsRepository);
        reviewStats.applyReviewGrade(showReview.getGrade());
    }

    @Transactional
    @OptimisticLock
    @Override
    public void cancel(ShowReview showReview) {
        final ShowReviewStats reviewStats = ShowReviewStatsHelper.getWithOptimisticLock(
                showReview.getShowId(), showReviewStatsRepository);
        reviewStats.cancelReviewGrade(showReview.getGrade());
    }
}
