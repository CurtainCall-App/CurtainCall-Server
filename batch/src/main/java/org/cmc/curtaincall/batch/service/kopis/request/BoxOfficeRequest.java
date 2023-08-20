package org.cmc.curtaincall.batch.service.kopis.request;

import org.cmc.curtaincall.domain.show.BoxOfficeGenre;
import org.cmc.curtaincall.domain.show.BoxOfficeType;

import java.time.LocalDate;

public record BoxOfficeRequest(
        BoxOfficeType type,                 // ststype
        BoxOfficeGenre genre,                // catecode
        LocalDate baseDate                   // date
) {
}
