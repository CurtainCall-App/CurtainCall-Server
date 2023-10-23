package org.cmc.curtaincall.web.review;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.OptimisticLock;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.review.*;
import org.cmc.curtaincall.domain.review.repository.ShowReviewRepository;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.web.review.request.ShowReviewCreate;
import org.cmc.curtaincall.web.review.request.ShowReviewEdit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShowReviewService {

    private final ShowReviewRepository showReviewRepository;

    private final ShowReviewGradeApplyService showReviewGradeApplyService;

    @Transactional
    public ShowReviewId create(ShowId showId, ShowReviewCreate showReviewCreate) {
        ShowReview showReview = showReviewRepository.save(ShowReview.builder()
                .showId(showId)
                .grade(showReviewCreate.getGrade())
                .content(showReviewCreate.getContent())
                .build());
        showReviewGradeApplyService.apply(showReview);
        return new ShowReviewId(showReview.getId());
    }

    @Transactional
    public void delete(ShowReviewId showReviewId) {
        ShowReview showReview = ShowReviewHelper.get(showReviewId, showReviewRepository);
        showReviewGradeApplyService.cancel(showReview);
        showReviewRepository.delete(showReview);
    }

    @Transactional
    @OptimisticLock
    public void edit(ShowReviewId id, ShowReviewEdit showReviewEdit) {
        ShowReview showReview = ShowReviewHelper.getWithOptimisticLock(id, showReviewRepository);
        showReviewGradeApplyService.cancel(showReview);

        ShowReviewEditor editor = showReview.toEditor()
                .grade(showReviewEdit.getGrade())
                .content(showReviewEdit.getContent())
                .build();

        showReview.edit(editor);
        showReviewGradeApplyService.apply(showReview);
    }

    public boolean isOwnedByMember(ShowReviewId id, MemberId memberId) {
        ShowReview showReview = ShowReviewHelper.get(id, showReviewRepository);
        return Objects.equals(showReview.getCreatedBy().getId(), memberId.getId());
    }

}
