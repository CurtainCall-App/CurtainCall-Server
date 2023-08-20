package org.cmc.curtaincall.batch.service.kopis.response;

import lombok.Builder;

@Builder
public record BoxOfficeResponse(
        String showId,
        String showName,
        String facilityName,
        int rank,
        int seatNum,
        String poster,
        String showPeriod,
        String genreName,
        int showCount,
        String area
) {
}
