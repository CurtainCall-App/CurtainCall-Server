package org.cmc.curtaincall.web.review.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.review.ShowReviewId;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShowReviewLikedResponse {

    private ShowReviewId showReviewId;

    private boolean liked;

    public ShowReviewLikedResponse(final ShowReviewId showReviewId, final boolean liked) {
        this.showReviewId = showReviewId;
        this.liked = liked;
    }
}
