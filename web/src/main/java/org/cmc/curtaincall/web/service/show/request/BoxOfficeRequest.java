package org.cmc.curtaincall.web.service.show.request;

import jakarta.validation.constraints.NotNull;
import org.cmc.curtaincall.domain.show.BoxOfficeGenre;
import org.cmc.curtaincall.domain.show.BoxOfficeType;

import java.time.LocalDate;

public record BoxOfficeRequest(
        @NotNull BoxOfficeType type,
        @NotNull BoxOfficeGenre genre,
        @NotNull LocalDate baseDate
) {
}
