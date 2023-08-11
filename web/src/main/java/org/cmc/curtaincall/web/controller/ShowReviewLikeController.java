package org.cmc.curtaincall.web.controller;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.web.security.annotation.LoginMemberId;
import org.cmc.curtaincall.web.service.review.ShowReviewLikeService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ShowReviewLikeController {

    private final ShowReviewLikeService showReviewLikeService;

    @PutMapping("/reviews/{reviewId}/like")
    public void likeReview(@PathVariable Long reviewId, @LoginMemberId Long memberId) {
        showReviewLikeService.like(memberId, reviewId);
    }

    @DeleteMapping("/reviews/{reviewId}/like")
    public void cancelLike(@PathVariable Long reviewId, @LoginMemberId Long memberId) {
        showReviewLikeService.cancelLike(memberId, reviewId);
    }
}
