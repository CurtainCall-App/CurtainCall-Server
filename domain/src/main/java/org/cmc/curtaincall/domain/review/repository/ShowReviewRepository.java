package org.cmc.curtaincall.domain.review.repository;

import jakarta.persistence.LockModeType;
import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.review.ShowReview;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface ShowReviewRepository extends JpaRepository<ShowReview, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    Optional<ShowReview> findWithLockById(Long id);

    @EntityGraph(attributePaths = {"show"})
    Slice<ShowReview> findSliceByCreatedByAndUseYnIsTrue(Pageable pageable, Member createdBy);
}
