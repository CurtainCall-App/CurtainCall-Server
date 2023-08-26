package org.cmc.curtaincall.web.service.show.response;

import lombok.Builder;
import org.cmc.curtaincall.domain.show.BoxOffice;
import org.cmc.curtaincall.domain.show.ShowGenre;

import java.time.LocalDate;

@Builder
public record BoxOfficeResponse(
        String id,                  // 공연 ID
        String name,                // 공연명
        LocalDate startDate,        // 공연 시작일
        LocalDate endDate,          // 공연 종료일
        String poster,              // 공연 포스터 경로
        ShowGenre genre,               // 공연 장르명
        long reviewGradeSum,               // 공연 장르명
        int reviewCount,               // 공연 장르명
        int rank
) {

    public static BoxOfficeResponse of(BoxOffice boxOffice) {
        return BoxOfficeResponse.builder()
                .id(boxOffice.getShow().getId())
                .name(boxOffice.getShow().getName())
                .startDate(boxOffice.getShow().getStartDate())
                .endDate(boxOffice.getShow().getEndDate())
                .poster(boxOffice.getShow().getPoster())
                .genre(boxOffice.getShow().getGenre())
                .reviewGradeSum(boxOffice.getShow().getReviewGradeSum())
                .reviewCount(boxOffice.getShow().getReviewCount())
                .rank(boxOffice.getRank())
                .build();
    }
}
