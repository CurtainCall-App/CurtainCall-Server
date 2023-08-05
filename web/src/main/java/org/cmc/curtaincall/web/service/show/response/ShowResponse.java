package org.cmc.curtaincall.web.service.show.response;

import lombok.*;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowTime;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ShowResponse {

    private String id;                  // 공연 ID

    private String name;                // 공연명

    private LocalDate startDate;        // 공연 시작일

    private LocalDate endDate;          // 공연 종료일

    private String facilityName;            // 공연 시설명(공연장명)

    private String poster;              // 공연 포스터 경로

    private ShowGenre genre;               // 공연 장르명

    private List<ShowTime> showTimes;
}