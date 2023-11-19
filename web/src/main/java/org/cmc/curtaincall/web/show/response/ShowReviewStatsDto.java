package org.cmc.curtaincall.web.show.response;

import lombok.Builder;
import org.cmc.curtaincall.domain.review.ShowReviewStats;
import org.cmc.curtaincall.domain.show.ShowId;

@Builder
public record ShowReviewStatsDto(
        ShowId showId,
        Integer reviewCount,
        Long reviewGradeSum,
        Double reviewGradeAvg
) {
    public static ShowReviewStatsDto of(final ShowReviewStats stats) {
        return ShowReviewStatsDto.builder()
                .showId(stats.getId())
                .reviewCount(stats.getReviewCount())
                .reviewGradeSum(stats.getReviewGradeSum())
                .reviewGradeAvg(stats.getReviewGradeAvg())
                .build();
    }
}
