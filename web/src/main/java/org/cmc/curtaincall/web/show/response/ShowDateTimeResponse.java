package org.cmc.curtaincall.web.show.response;

import lombok.Builder;
import org.cmc.curtaincall.domain.show.Facility;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.ShowDateTime;
import org.cmc.curtaincall.domain.show.ShowGenre;

import java.time.LocalDateTime;

@Builder
public record ShowDateTimeResponse(
        String id,
        String name,
        String facilityId,
        String facilityName,
        String poster,
        ShowGenre genre,
        LocalDateTime showAt,
        LocalDateTime showEndAt
) {

    public static ShowDateTimeResponse of(ShowDateTime showDateTime) {
        Show show = showDateTime.getShow();
        Facility facility = show.getFacility();
        return ShowDateTimeResponse.builder()
                .id(show.getId().getId())
                .name(show.getName())
                .facilityId(facility.getId())
                .facilityName(facility.getName())
                .poster(show.getPoster())
                .genre(show.getGenre())
                .showAt(showDateTime.getShowAt())
                .showEndAt(showDateTime.getShowEndAt())
                .build();
    }
}
