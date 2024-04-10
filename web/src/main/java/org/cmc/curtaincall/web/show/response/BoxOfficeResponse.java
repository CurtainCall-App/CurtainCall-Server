package org.cmc.curtaincall.web.show.response;

import lombok.Builder;
import org.cmc.curtaincall.domain.show.ShowId;

@Builder
public record BoxOfficeResponse(
        ShowId id,                  // 공연 ID
        int rank
) {
}
