package org.cmc.curtaincall.web.show.response;

import lombok.*;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowTime;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FavoriteShowResponse {

    private String id;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private String facilityName;

    private String poster;

    private ShowGenre genre;

    private List<ShowTime> showTimes;

    private Integer reviewCount;

    private Long reviewGradeSum;

    private String runtime;
}
