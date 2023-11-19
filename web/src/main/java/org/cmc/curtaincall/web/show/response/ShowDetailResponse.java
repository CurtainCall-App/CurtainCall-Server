package org.cmc.curtaincall.web.show.response;

import lombok.*;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowTime;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ShowDetailResponse {

    private String id;                  // 공연 ID

    private String name;                // 공연명

    private LocalDate startDate;        // 공연 시작일

    private LocalDate endDate;          // 공연 종료일

    private String facilityId;          // 공연 시설 ID

    private String facilityName;            // 공연 시설명 (공연장명)

    private String crew;              // 공연 제작진

    private String cast;              // 공연 출연진

    private String runtime;              // 공연 런타임

    private String age;              // 공연 관람 연령

    private String enterprise;              // 제작사

    private String ticketPrice;              // 티켓 가격

    private String poster;              // 공연 포스터 경로

    private String story;               // 줄거리

    private ShowGenre genre;               // 공연 장르명

    private List<String> introductionImages;    // 소개 이미지

    private List<ShowTime> showTimes;             // 공연 시간

    private Integer reviewCount;

    private Long reviewGradeSum;

    public static ShowDetailResponse of(Show show) {
        return ShowDetailResponse.builder()
                .id(show.getId().getId())
                .name(show.getName())
                .startDate(show.getStartDate())
                .endDate(show.getEndDate())
                .facilityId(show.getFacility().getId())
                .facilityName(show.getFacility().getName())
                .crew(show.getCrew())
                .cast(show.getCast())
                .runtime(show.getRuntime())
                .age(show.getAge())
                .enterprise(show.getEnterprise())
                .ticketPrice(show.getTicketPrice())
                .poster(show.getPoster())
                .story(show.getStory())
                .genre(show.getGenre())
                .introductionImages(new ArrayList<>(show.getIntroductionImages()))
                .showTimes(new ArrayList<>(show.getShowTimes()))
                .reviewCount(show.getReviewCount())
                .reviewGradeSum(show.getReviewGradeSum())
                .build();
    }
}
