package org.cmc.curtaincall.web.controller;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.web.exception.EntityAccessDeniedException;
import org.cmc.curtaincall.web.security.annotation.LoginMemberId;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.review.ShowReviewService;
import org.cmc.curtaincall.web.service.review.request.ShowReviewCreate;
import org.cmc.curtaincall.web.service.review.request.ShowReviewEdit;
import org.cmc.curtaincall.web.service.review.response.ShowReviewResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
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

    @GetMapping("/shows/{showId}/reviews")
    public Slice<ShowReviewResponse> getList(
            @SortDefault(sort = "likeCount", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable String showId) {
        return showReviewService.getList(pageable, showId);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public void deleteReview(@PathVariable Long reviewId, @LoginMemberId Long memberId) {
        if (!showReviewService.isOwnedByMember(reviewId, memberId)) {
            throw new EntityAccessDeniedException("reviewId=" + reviewId + "memberId=" + memberId);
        }
        showReviewService.delete(reviewId);
    }

    @PatchMapping("/reviews/{reviewId}")
    public void editReview(
            @PathVariable Long reviewId, @LoginMemberId Long memberId,
            @RequestBody @Validated ShowReviewEdit showReviewEdit) {
        if (!showReviewService.isOwnedByMember(reviewId, memberId)) {
            throw new EntityAccessDeniedException("reviewId=" + reviewId + "memberId=" + memberId);
        }
        showReviewService.edit(showReviewEdit, reviewId);
    }
}
