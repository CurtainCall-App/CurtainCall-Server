package org.cmc.curtaincall.web.service.review.response;

import lombok.Builder;

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

}
