package org.cmc.curtaincall.domain.review.dao;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.common.RepositoryHelper;
import org.cmc.curtaincall.domain.review.response.QShowReviewResponse;
import org.cmc.curtaincall.domain.review.response.ShowReviewResponse;
import org.cmc.curtaincall.domain.show.ShowId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static org.cmc.curtaincall.domain.member.QMember.member;
import static org.cmc.curtaincall.domain.review.QShowReview.showReview;

@Repository
@RequiredArgsConstructor
public class ShowReviewDao {

    private final JPAQueryFactory query;

    public List<ShowReviewResponse> findAllByShowId(Pageable pageable, ShowId showId) {

        return query.select(new QShowReviewResponse(
                        showReview.id,
                        showReview.showId.id,
                        showReview.grade,
                        showReview.content,
                        showReview.createdBy.id,
                        member.nickname,
                        member.image.url,
                        showReview.createdAt,
                        showReview.likeCount
                ))
                .from(showReview)
                .innerJoin(member).on(showReview.createdBy.id.eq(member.id))
                .leftJoin(member.image)
                .where(showReview.showId.id.eq(showId.getId()))
                .orderBy(
                        RepositoryHelper.filterNullOrderByArr(
                                getLikeCountOrder(pageable),
                                getCreatedAtOrder(pageable)
                        )
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1L)
                .fetch();
    }

    private OrderSpecifier<Integer> getLikeCountOrder(Pageable pageable) {
        Sort.Order sortOrder = pageable.getSort().getOrderFor("likeCount");
        if (sortOrder == null) {
            return null;
        }
        Order order = Order.valueOf(sortOrder.getDirection().name());
        return new OrderSpecifier<>(order, showReview.likeCount);
    }

    private OrderSpecifier<LocalDateTime> getCreatedAtOrder(Pageable pageable) {
        Sort.Order sortOrder = pageable.getSort().getOrderFor("createdAt");
        if (sortOrder == null) {
            return null;
        }
        Order order = Order.valueOf(sortOrder.getDirection().name());
        return new OrderSpecifier<>(order, showReview.createdAt);
    }

}
