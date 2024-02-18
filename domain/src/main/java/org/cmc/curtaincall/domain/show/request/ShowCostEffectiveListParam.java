package org.cmc.curtaincall.domain.show.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.cmc.curtaincall.domain.show.ShowGenre;

@Builder
public record ShowCostEffectiveListParam(
        @NotNull ShowGenre genre
) {
}
