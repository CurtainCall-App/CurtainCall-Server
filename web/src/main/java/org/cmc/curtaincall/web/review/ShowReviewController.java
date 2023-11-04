package org.cmc.curtaincall.web.review;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.review.ShowReviewId;
import org.cmc.curtaincall.domain.review.validation.ShowReviewCreatorValidator;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.web.common.response.IdResult;
import org.cmc.curtaincall.web.review.request.ShowReviewCreate;
import org.cmc.curtaincall.web.review.request.ShowReviewCreateDepr;
import org.cmc.curtaincall.web.review.request.ShowReviewEdit;
import org.cmc.curtaincall.web.security.LoginMemberId;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
            @PathVariable ShowId showId, @Validated @RequestBody ShowReviewCreateDepr showReviewCreate) {
        ShowReviewId showReviewId = showReviewService.create(showId, showReviewCreate);
        return new IdResult<>(showReviewId.getId());
    }

    @DeleteMapping("/reviews/{reviewId}")
    public void delete(@PathVariable ShowReviewId reviewId, @LoginMemberId MemberId memberId) {
        showReviewCreatorValidator.validate(reviewId, new CreatorId(memberId));
        showReviewService.delete(reviewId);
    }

    @PatchMapping("/reviews/{reviewId}")
    public void edit(
            @PathVariable ShowReviewId reviewId, @LoginMemberId MemberId memberId,
            @RequestBody @Validated ShowReviewEdit showReviewEdit) {
        showReviewCreatorValidator.validate(reviewId, new CreatorId(memberId));
        showReviewService.edit(reviewId, showReviewEdit);
    }
}
