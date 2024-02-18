package org.cmc.curtaincall.domain.show.dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowState;
import org.cmc.curtaincall.domain.show.request.ShowCostEffectiveListParam;
import org.cmc.curtaincall.domain.show.response.QShowCostEffectiveResponse;
import org.cmc.curtaincall.domain.show.response.ShowCostEffectiveResponse;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Random;

import static org.cmc.curtaincall.domain.show.QShow.show;

@Repository
public class ShowCostEffectiveDao {

    private static final int SIZE = 10;

    private final JPAQueryFactory query;

    private final Random random = new Random();

    public ShowCostEffectiveDao(final EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public List<ShowCostEffectiveResponse> getList(final ShowCostEffectiveListParam param) {
        final int priceLessThan = param.genre() == ShowGenre.MUSICAL ? 70_000 : 30_000;
        final Predicate[] predicates = {
                show.state.eq(ShowState.PERFORMING),
                show.kidState.isFalse(),
                show.genre.eq(param.genre()),
                show.minTicketPrice.lt(priceLessThan)
        };
        final long count = query.select(show.count())
                .from(show)
                .where(predicates)
                .fetchOne();
        return query.select(new QShowCostEffectiveResponse(
                        show.id,
                        show.name,
                        show.startDate,
                        show.endDate,
                        show.poster,
                        show.genre,
                        show.minTicketPrice
        ))
                .from(show)
                .where(predicates)
                .offset(getRandomOffset(count))
                .limit(SIZE)
                .fetch();
    }

    private long getRandomOffset(final long count) {
        return random.nextLong(count - SIZE);
    }
}
