package org.cmc.curtaincall.web.service.review.response;

import lombok.Builder;
import org.cmc.curtaincall.domain.image.Image;
import org.cmc.curtaincall.domain.review.ShowReview;

import java.time.LocalDateTime;
import java.util.Optional;

@Builder
public record ShowReviewResponse (
        Long id,
        String showId,
        Integer grade,
        String content,
        Long creatorId,
        String creatorNickname,
        String creatorImageUrl,
        LocalDateTime createdAt,
        Integer likeCount
) {

    public static ShowReviewResponse of(ShowReview showReview) {
        return ShowReviewResponse.builder()
                .id(showReview.getId())
                .showId(showReview.getShowId().getId())
                .grade(showReview.getGrade())
                .content(showReview.getContent())
                .creatorId(showReview.getCreatedBy().getId())
                .creatorNickname(showReview.getCreatedBy().getNickname())
                .creatorImageUrl(Optional.ofNullable(showReview.getCreatedBy().getImage())
                        .filter(Image::getUseYn)
                        .map(Image::getUrl)
                        .orElse(null))
                .createdAt(showReview.getCreatedAt())
                .likeCount(showReview.getLikeCount())
                .build();
    }
}
