package org.cmc.curtaincall.domain.show.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowId;

import java.time.LocalDate;

@Getter
@ToString
public class ShowCostEffectiveResponse {

    private ShowId id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String poster;
    private ShowGenre genre;
    private Integer minTicketPrice;

    @Builder
    @QueryProjection
    public ShowCostEffectiveResponse(
            ShowId id,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            String poster,
            ShowGenre genre,
            Integer minTicketPrice
    ) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.poster = poster;
        this.genre = genre;
        this.minTicketPrice = minTicketPrice;
    }
}
