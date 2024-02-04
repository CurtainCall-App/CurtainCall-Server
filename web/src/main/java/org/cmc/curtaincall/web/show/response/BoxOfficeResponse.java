package org.cmc.curtaincall.web.show.response;

import lombok.Builder;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.domain.show.ShowTime;

import java.time.LocalDate;
import java.util.List;

@Builder
public record BoxOfficeResponse(
        ShowId id,                  // 공연 ID
        String name,                // 공연명
        LocalDate startDate,        // 공연 시작일
        LocalDate endDate,          // 공연 종료일
        String facilityName,            // 공연 시설명(공연장명)
        String poster,              // 공연 포스터 경로
        ShowGenre genre,               // 공연 장르명
        List<ShowTime> showTimes,
        String runtime,
        int rank
) {
}
