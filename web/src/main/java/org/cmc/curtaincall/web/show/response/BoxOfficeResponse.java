package org.cmc.curtaincall.web.show.response;

import lombok.Builder;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowId;

import java.time.LocalDate;

@Builder
public record BoxOfficeResponse(
        ShowId id,                  // 공연 ID
        String name,                // 공연명
        LocalDate startDate,        // 공연 시작일
        LocalDate endDate,          // 공연 종료일
        String poster,              // 공연 포스터 경로
        ShowGenre genre,               // 공연 장르명
        int rank
) {
}
