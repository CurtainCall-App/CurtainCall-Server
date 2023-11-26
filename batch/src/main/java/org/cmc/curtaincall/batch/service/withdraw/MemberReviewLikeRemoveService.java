package org.cmc.curtaincall.batch.service.withdraw;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.OptimisticLock;
import org.cmc.curtaincall.domain.review.ShowReview;
import org.cmc.curtaincall.domain.review.ShowReviewHelper;
import org.cmc.curtaincall.domain.review.ShowReviewId;
import org.cmc.curtaincall.domain.review.ShowReviewLike;
import org.cmc.curtaincall.domain.review.repository.ShowReviewLikeRepository;
import org.cmc.curtaincall.domain.review.repository.ShowReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberReviewLikeRemoveService {

    private final ShowReviewLikeRepository showReviewLikeRepository;

    private final ShowReviewRepository showReviewRepository;

    @OptimisticLock
    @Transactional
    public void remove(ShowReviewLike like) {
        final ShowReviewId showReviewId = new ShowReviewId(like.getShowReview().getId());
        final ShowReview showReview = ShowReviewHelper.getWithOptimisticLock(
                showReviewId, showReviewRepository);
        showReview.cancelLike(like.getMemberId(), showReviewLikeRepository);
    }
}
