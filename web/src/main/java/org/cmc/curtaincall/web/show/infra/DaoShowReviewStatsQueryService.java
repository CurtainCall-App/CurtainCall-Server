package org.cmc.curtaincall.web.show.infra;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.review.repository.ShowReviewStatsRepository;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.web.show.ShowReviewStatsQueryService;
import org.cmc.curtaincall.web.show.response.ShowReviewStatsDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DaoShowReviewStatsQueryService implements ShowReviewStatsQueryService {

    private final ShowReviewStatsRepository showReviewStatsRepository;

    @Override
    public ShowReviewStatsDto get(final ShowId showId) {
        return showReviewStatsRepository.findById(showId)
                .map(ShowReviewStatsDto::of)
                .orElse(new ShowReviewStatsDto(showId, 0, 0L, 0D));
    }
}
