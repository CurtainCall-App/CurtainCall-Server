package org.cmc.curtaincall.domain.review.repository;

import org.cmc.curtaincall.domain.review.ShowReview;
import org.cmc.curtaincall.domain.show.Show;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowReviewRepository extends JpaRepository<ShowReview, Long> {

    @EntityGraph(attributePaths = {"createdBy", "createdBy.image"})
    Slice<ShowReview> findSliceByShowAndUseYnIsTrue(Pageable pageable, Show show);
}
