package org.cmc.curtaincall.web.controller;

import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.web.security.annotation.LoginMemberId;
import org.cmc.curtaincall.web.service.review.ShowReviewLikeService;
import org.cmc.curtaincall.web.service.review.response.ShowReviewLikedResponse;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/member/like")
    public Slice<ShowReviewLikedResponse> getLiked(
            @RequestParam @Validated @Size(max = 100) List<Long> reviewIds, @LoginMemberId Long memberId) {
        List<ShowReviewLikedResponse> showReviewLikedResponses = showReviewLikeService.areLiked(memberId, reviewIds);
        return new SliceImpl<>(showReviewLikedResponses);
    }
}
