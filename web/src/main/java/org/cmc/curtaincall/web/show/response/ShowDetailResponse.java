package org.cmc.curtaincall.web.show.response;

import lombok.Builder;
import org.cmc.curtaincall.domain.show.FacilityId;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.domain.show.ShowTime;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
public record ShowDetailResponse (
        ShowId id,                  // 공연 ID
        String name,                // 공연명
        LocalDate startDate,        // 공연 시작일
        LocalDate endDate,          // 공연 종료일
        FacilityId facilityId,          // 공연 시설 ID
        String facilityName,            // 공연 시설명 (공연장명)
        String crew,              // 공연 제작진
        String cast,              // 공연 출연진
        String runtime,              // 공연 런타임
        String age,              // 공연 관람 연령
        String enterprise,              // 제작사
        String ticketPrice,              // 티켓 가격
        String poster,              // 공연 포스터 경로
        String story,               // 줄거리
        ShowGenre genre,               // 공연 장르명
        List<String> introductionImages,    // 소개 이미지
        List<ShowTime> showTimes             // 공연 시간
) {

    public static ShowDetailResponse of(final Show show) {
        return ShowDetailResponse.builder()
                .id(show.getId())
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
                .build();
    }
}
