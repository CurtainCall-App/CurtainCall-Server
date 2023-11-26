package org.cmc.curtaincall.domain.review.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.cmc.curtaincall.domain.common.QuerydslHelper;
import org.cmc.curtaincall.domain.review.ShowReviewStats;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowState;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.cmc.curtaincall.domain.review.QShowReviewStats.showReviewStats;
import static org.cmc.curtaincall.domain.show.QShow.show;

@Repository
public class ShowReviewStatsCustomImpl implements ShowReviewStatsRepositoryCustom {

    private final JPAQueryFactory query;

    public ShowReviewStatsCustomImpl(final EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public List<ShowReviewStats> findAllByGenreAndStateAndUseYnIsTrue(
            final Pageable pageable, @Nullable final ShowGenre genre, @Nullable final ShowState state
    ) {
        return query.selectFrom(showReviewStats)
                .join(show).on(showReviewStats.id.eq(show.id))
                .where(
                        showGenreEq(genre),
                        showStateEq(state),
                        showReviewStats.useYn
                )
                .orderBy(
                        QuerydslHelper.filterNullOrderByArr(
                                getReviewGradeAvgOrder(pageable)
                        )
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private static BooleanExpression showStateEq(final ShowState state) {
        return Optional.ofNullable(state).map(show.state::eq).orElse(null);
    }

    private static BooleanExpression showGenreEq(final ShowGenre genre) {
        return Optional.ofNullable(genre).map(show.genre::eq).orElse(null);
    }

    private OrderSpecifier<Double> getReviewGradeAvgOrder(final Pageable pageable) {
        Sort.Order sortOrder = pageable.getSort().getOrderFor("reviewGradeAvg");
        if (sortOrder == null) {
            return null;
        }
        Order order = Order.valueOf(sortOrder.getDirection().name());
        return new OrderSpecifier<>(order, showReviewStats.reviewGradeAvg);
    }

}
