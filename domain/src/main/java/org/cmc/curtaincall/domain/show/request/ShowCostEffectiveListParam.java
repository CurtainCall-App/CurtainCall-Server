package org.cmc.curtaincall.domain.show.request;

import lombok.Builder;
import org.cmc.curtaincall.domain.show.ShowGenre;

@Builder
public record ShowCostEffectiveListParam(
        ShowGenre genre
) {
}
