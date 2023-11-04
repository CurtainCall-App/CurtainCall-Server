package org.cmc.curtaincall.domain.review.dao;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.common.QuerydslHelper;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.review.response.QShowReviewMyResponse;
import org.cmc.curtaincall.domain.review.response.QShowReviewResponse;
import org.cmc.curtaincall.domain.review.response.ShowReviewMyResponse;
import org.cmc.curtaincall.domain.review.response.ShowReviewResponse;
import org.cmc.curtaincall.domain.show.ShowId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static org.cmc.curtaincall.domain.member.QMember.member;
import static org.cmc.curtaincall.domain.review.QShowReview.showReview;
import static org.cmc.curtaincall.domain.show.QShow.show;

@Repository
@RequiredArgsConstructor
public class ShowReviewDao {

    private final JPAQueryFactory query;

    public List<ShowReviewResponse> getList(Pageable pageable, ShowId showId) {
        return query.select(new QShowReviewResponse(
                        showReview.id,
                        showReview.showId.id,
                        showReview.grade,
                        showReview.content,
                        showReview.createdBy,
                        member.nickname,
                        member.image.url,
                        showReview.createdAt,
                        showReview.likeCount
                ))
                .from(showReview)
                .innerJoin(member).on(showReview.createdBy.memberId.id.eq(member.id))
                .leftJoin(member.image)
                .where(showReview.showId.id.eq(showId.getId()))
                .orderBy(
                        QuerydslHelper.filterNullOrderByArr(
                                getLikeCountOrder(pageable),
                                getCreatedAtOrder(pageable)
                        )
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
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

    public List<ShowReviewMyResponse> getMyList(Pageable pageable, CreatorId creatorId) {
        return query.select(new QShowReviewMyResponse(
                        showReview.id,
                        showReview.showId.id,
                        show.name,
                        showReview.grade,
                        showReview.content,
                        showReview.createdAt,
                        showReview.likeCount
                ))
                .from(showReview)
                .join(show).on(showReview.showId.id.eq(show.id))
                .where(showReview.createdBy.memberId.id.eq(creatorId.getId()))
                .orderBy(
                        QuerydslHelper.filterNullOrderByArr(
                                getCreatedAtOrder(pageable)
                        )
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

}
