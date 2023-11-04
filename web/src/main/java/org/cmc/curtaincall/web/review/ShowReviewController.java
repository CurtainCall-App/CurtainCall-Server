package org.cmc.curtaincall.web.review;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.review.ShowReviewId;
import org.cmc.curtaincall.domain.review.validation.ShowReviewCreatorValidator;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.web.common.response.IdResult;
import org.cmc.curtaincall.web.exception.EntityAccessDeniedException;
import org.cmc.curtaincall.web.review.request.ShowReviewCreate;
import org.cmc.curtaincall.web.review.request.ShowReviewCreateDepr;
import org.cmc.curtaincall.web.review.request.ShowReviewEdit;
import org.cmc.curtaincall.web.security.LoginMemberId;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ShowReviewController {

    private final ShowReviewService showReviewService;

    private final ShowReviewCreatorValidator showReviewCreatorValidator;

    @PostMapping("/review")
    public IdResult<Long> create(@Validated @RequestBody ShowReviewCreate showReviewCreate) {
        return new IdResult<>(showReviewService.create(showReviewCreate).getId());
    }

    @PostMapping("/shows/{showId}/reviews")
    public IdResult<Long> createShowReview(
            @PathVariable String showId, @Validated @RequestBody ShowReviewCreateDepr showReviewCreate) {
        ShowReviewId showReviewId = showReviewService.create(new ShowId(showId), showReviewCreate);
        return new IdResult<>(showReviewId.getId());
    }

    @DeleteMapping("/reviews/{reviewId}")
    public void delete(@PathVariable Long reviewId, @LoginMemberId MemberId memberId) {
        ShowReviewId id = new ShowReviewId(reviewId);
        showReviewCreatorValidator.validate(id, new CreatorId(memberId));
        showReviewService.delete(id);
    }

    @PatchMapping("/reviews/{reviewId}")
    public void editReview(
            @PathVariable Long reviewId, @LoginMemberId MemberId memberId,
            @RequestBody @Validated ShowReviewEdit showReviewEdit) {
        ShowReviewId id = new ShowReviewId(reviewId);
        if (!showReviewService.isOwnedByMember(id, memberId)) {
            throw new EntityAccessDeniedException("reviewId=" + reviewId + "memberId=" + memberId);
        }
        showReviewService.edit(id, showReviewEdit);
    }
}
