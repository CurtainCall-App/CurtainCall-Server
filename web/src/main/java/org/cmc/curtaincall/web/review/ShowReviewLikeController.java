package org.cmc.curtaincall.web.review;

import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.review.ShowReviewId;
import org.cmc.curtaincall.web.common.response.ListResult;
import org.cmc.curtaincall.web.review.response.ShowReviewLikedResponse;
import org.cmc.curtaincall.web.security.LoginMemberId;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ShowReviewLikeController {

    private final ShowReviewLikeService showReviewLikeService;

    @PutMapping("/reviews/{reviewId}/like")
    public void likeReview(@PathVariable Long reviewId, @LoginMemberId MemberId memberId) {
        showReviewLikeService.like(memberId, new ShowReviewId(reviewId));
    }

    @DeleteMapping("/reviews/{reviewId}/like")
    public void cancelLike(@PathVariable Long reviewId, @LoginMemberId MemberId memberId) {
        showReviewLikeService.cancelLike(memberId, new ShowReviewId(reviewId));
    }

    @GetMapping("/member/like")
    public ListResult<ShowReviewLikedResponse> getLiked(
            @RequestParam @Validated @Size(max = 100) List<Long> reviewIds, @LoginMemberId MemberId memberId) {
        List<ShowReviewLikedResponse> showReviewLikedResponses = showReviewLikeService.areLiked(
                memberId, reviewIds.stream().map(ShowReviewId::new).toList());
        return new ListResult<>(showReviewLikedResponses);
    }
}
