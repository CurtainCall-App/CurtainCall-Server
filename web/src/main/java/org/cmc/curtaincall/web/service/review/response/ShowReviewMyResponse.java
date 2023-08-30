package org.cmc.curtaincall.web.service.review.response;

import lombok.Builder;
import org.cmc.curtaincall.domain.review.ShowReview;

import java.time.LocalDateTime;

@Builder
public record ShowReviewMyResponse(
        Long id,
        String showId,
        String showName,
        Integer grade,
        String content,
        LocalDateTime createdAt,
        Integer likeCount
) {

    public static ShowReviewMyResponse of(ShowReview showReview) {
        return ShowReviewMyResponse.builder()
                .id(showReview.getId())
                .showId(showReview.getShow().getId())
                .showName(showReview.getShow().getName())
                .grade(showReview.getGrade())
                .content(showReview.getContent())
                .createdAt(showReview.getCreatedAt())
                .likeCount(showReview.getLikeCount())
                .build();
    }
}
