package org.cmc.curtaincall.web.review;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.review.ShowReview;
import org.cmc.curtaincall.domain.review.ShowReviewEditor;
import org.cmc.curtaincall.domain.review.ShowReviewGradeApplyService;
import org.cmc.curtaincall.domain.review.ShowReviewHelper;
import org.cmc.curtaincall.domain.review.ShowReviewId;
import org.cmc.curtaincall.domain.review.exception.ShowReviewAlreadyReviewedException;
import org.cmc.curtaincall.domain.review.repository.ShowReviewRepository;
import org.cmc.curtaincall.domain.review.validation.ShowReviewShowValidator;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.web.review.request.ShowReviewCreate;
import org.cmc.curtaincall.web.review.request.ShowReviewEdit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShowReviewService {

    private final ShowReviewRepository showReviewRepository;

    private final ShowReviewGradeApplyService showReviewGradeApplyService;

    private final ShowReviewShowValidator showReviewShowValidator;

    @Transactional
    public ShowReviewId create(final ShowReviewCreate showReviewCreate, final CreatorId creatorId) {
        ShowId showId = showReviewCreate.getShowId();
        showReviewShowValidator.validate(showId);
        if (showReviewRepository.existsByShowIdAndCreatedByAndUseYnIsTrue(showId, creatorId)) {
            throw new ShowReviewAlreadyReviewedException(showId, creatorId);
        }
        ShowReview showReview = showReviewRepository.save(ShowReview.builder()
                .showId(showId)
                .grade(showReviewCreate.getGrade())
                .content(showReviewCreate.getContent())
                .createdBy(creatorId)
                .build());
        showReviewGradeApplyService.apply(showReview);
        return new ShowReviewId(showReview.getId());
    }

    @Transactional
    public void edit(final ShowReviewId id, final ShowReviewEdit showReviewEdit) {
        ShowReview showReview = ShowReviewHelper.getWithOptimisticLock(id, showReviewRepository);
        showReviewGradeApplyService.cancel(showReview);

        ShowReviewEditor editor = showReview.toEditor()
                .grade(showReviewEdit.getGrade())
                .content(showReviewEdit.getContent())
                .build();

        showReview.edit(editor);
        showReviewGradeApplyService.apply(showReview);
    }

}
