package org.cmc.curtaincall.web.show;

import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.web.show.response.ShowReviewStatsDto;

import java.util.List;

public interface ShowReviewStatsQueryService {

    ShowReviewStatsDto get(ShowId showId);

    List<ShowReviewStatsDto> getList(List<ShowId> showIds);
}
