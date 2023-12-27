package org.cmc.curtaincall.web.show.response;

import lombok.Builder;

@Builder
public record ShowReviewStatsResponse(
        Integer reviewCount,
        Long reviewGradeSum,
        Double reviewGradeAvg
) {
    public static ShowReviewStatsResponse EMPTY = new ShowReviewStatsResponse(0, 0L, 0D);

    public static ShowReviewStatsResponse of(final ShowReviewStatsDto stats) {
        return ShowReviewStatsResponse.builder()
                .reviewCount(stats.reviewCount())
                .reviewGradeSum(stats.reviewGradeSum())
                .reviewGradeAvg(stats.reviewGradeAvg())
                .build();
    }
}
