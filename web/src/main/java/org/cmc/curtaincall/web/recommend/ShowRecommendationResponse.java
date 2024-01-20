package org.cmc.curtaincall.web.recommend;


import lombok.Builder;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowId;

import java.time.LocalDate;

@Builder
public record ShowRecommendationResponse(
        Long id,
        String description,
        ShowId showId,
        String name,
        ShowGenre genre,
        LocalDate startDate,
        LocalDate endDate
) {
}
