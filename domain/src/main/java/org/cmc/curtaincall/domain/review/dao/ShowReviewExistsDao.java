package org.cmc.curtaincall.domain.review.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.review.ShowReviewId;
import org.springframework.stereotype.Repository;

import static org.cmc.curtaincall.domain.review.QShowReview.showReview;

@Repository
@RequiredArgsConstructor
public class ShowReviewExistsDao {

    private final JPAQueryFactory query;

    public boolean existsByIdAndCreatedBy(final ShowReviewId id, final CreatorId creatorId) {
        return query
                .selectOne()
                .from(showReview)
                .where(
                        showReview.id.eq(id.getId()),
                        showReview.createdBy.eq(creatorId),
                        showReview.useYn.isTrue()
                )
                .fetchFirst() != null;
    }
}
