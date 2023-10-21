package org.cmc.curtaincall.web.review;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.web.common.response.IdResult;
import org.cmc.curtaincall.web.exception.EntityAccessDeniedException;
import org.cmc.curtaincall.web.review.request.ShowReviewCreate;
import org.cmc.curtaincall.web.review.request.ShowReviewEdit;
import org.cmc.curtaincall.web.security.annotation.LoginMemberId;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ShowReviewController {

    private final ShowReviewService showReviewService;

    @PostMapping("/shows/{showId}/reviews")
    public IdResult<Long> createShowReview(
            @PathVariable String showId, @Validated @RequestBody ShowReviewCreate showReviewCreate) {
        return showReviewService.create(showId, showReviewCreate);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public void deleteReview(@PathVariable Long reviewId, @LoginMemberId MemberId memberId) {
        if (!showReviewService.isOwnedByMember(reviewId, memberId.getId())) {
            throw new EntityAccessDeniedException("reviewId=" + reviewId + "memberId=" + memberId);
        }
        showReviewService.delete(reviewId);
    }

    @PatchMapping("/reviews/{reviewId}")
    public void editReview(
            @PathVariable Long reviewId, @LoginMemberId MemberId memberId,
            @RequestBody @Validated ShowReviewEdit showReviewEdit) {
        if (!showReviewService.isOwnedByMember(reviewId, memberId.getId())) {
            throw new EntityAccessDeniedException("reviewId=" + reviewId + "memberId=" + memberId);
        }
        showReviewService.edit(showReviewEdit, reviewId);
    }
}
