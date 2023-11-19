package org.cmc.curtaincall.domain.show.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.show.ShowId;
import org.springframework.stereotype.Repository;

import static org.cmc.curtaincall.domain.show.QShow.show;

@Repository
@RequiredArgsConstructor
public class ShowExistsDao {

    private final JPAQueryFactory query;

    public boolean exists(final ShowId showId) {
        return query
                .selectOne()
                .from(show)
                .where(
                        show.id.eq(showId),
                        show.useYn.isTrue()
                )
                .fetchFirst() != null;
    }
}
