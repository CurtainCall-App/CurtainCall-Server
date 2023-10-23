package org.cmc.curtaincall.domain.review.infra;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.OptimisticLock;
import org.cmc.curtaincall.domain.review.ShowReview;
import org.cmc.curtaincall.domain.review.ShowReviewGradeApplyService;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.ShowHelper;
import org.cmc.curtaincall.domain.show.repository.ShowRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShowReviewGradeApplyServiceImpl implements ShowReviewGradeApplyService {

    private final ShowRepository showRepository;

    @Transactional
    @OptimisticLock
    @Override
    public void apply(final ShowReview showReview) {
        Show show = ShowHelper.getWithOptimisticLock(showReview.getShowId(), showRepository);
        show.applyReviewGrade(showReview.getGrade());
    }

    @Transactional
    @OptimisticLock
    @Override
    public void cancel(ShowReview showReview) {
        Show show = ShowHelper.getWithOptimisticLock(showReview.getShowId(), showRepository);
        show.cancelReviewGrade(showReview.getGrade());
    }
}
