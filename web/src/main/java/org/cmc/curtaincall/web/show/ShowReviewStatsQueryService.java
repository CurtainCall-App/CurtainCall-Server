package org.cmc.curtaincall.web.show;

import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.web.show.response.ShowReviewStatsDto;

public interface ShowReviewStatsQueryService {

    ShowReviewStatsDto get(ShowId showId);
}
