package org.cmc.curtaincall.web.review;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.review.ShowReview;
import org.cmc.curtaincall.domain.review.ShowReviewGradeApplyService;
import org.cmc.curtaincall.domain.review.ShowReviewId;
import org.cmc.curtaincall.domain.review.exception.ShowReviewNotFoundException;
import org.cmc.curtaincall.domain.review.repository.ShowReviewLikeRepository;
import org.cmc.curtaincall.domain.review.repository.ShowReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ShowReviewDeleteService {

    private final ShowReviewGradeApplyService showReviewGradeApplyService;

    private final ShowReviewRepository showReviewRepository;

    private final ShowReviewLikeRepository showReviewLikeRepository;

    @Transactional
    public void delete(final ShowReviewId showReviewId) {
        final ShowReview showReview = showReviewRepository.findWithPessimisticLockById(showReviewId.getId())
                .orElseThrow(() -> new ShowReviewNotFoundException(showReviewId));
        showReviewGradeApplyService.cancel(showReview);
        showReviewLikeRepository.deleteAllByShowReview(showReview);
        showReviewRepository.delete(showReview);
    }

}
