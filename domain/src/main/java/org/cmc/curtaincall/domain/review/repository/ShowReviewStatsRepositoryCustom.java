package org.cmc.curtaincall.domain.review.repository;

import org.cmc.curtaincall.domain.review.ShowReviewStats;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowState;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import java.util.List;

public interface ShowReviewStatsRepositoryCustom {

    List<ShowReviewStats> findAllByGenreAndStateAndUseYnIsTrue(
            final Pageable pageable, @Nullable final ShowGenre genre, @Nullable final ShowState state
    );
}
