package org.cmc.curtaincall.domain.review.repository;

import jakarta.persistence.LockModeType;
import org.cmc.curtaincall.domain.review.ShowReviewStats;
import org.cmc.curtaincall.domain.show.ShowId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface ShowReviewStatsRepository extends JpaRepository<ShowReviewStats, ShowId>, ShowReviewStatsRepositoryCustom{

    @Lock(LockModeType.OPTIMISTIC)
    Optional<ShowReviewStats> findWithLockById(ShowId id);

    @Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
    Optional<ShowReviewStats> findWithPessimisticLockById(ShowId id);
}
