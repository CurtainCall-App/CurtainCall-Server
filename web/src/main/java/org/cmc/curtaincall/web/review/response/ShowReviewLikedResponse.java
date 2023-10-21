package org.cmc.curtaincall.web.review.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShowReviewLikedResponse {

    private Long showReviewId;

    private boolean liked;

    public ShowReviewLikedResponse(Long showReviewId, boolean liked) {
        this.showReviewId = showReviewId;
        this.liked = liked;
    }
}
