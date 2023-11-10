package org.cmc.curtaincall.domain.lostitem.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.lostitem.LostItemId;
import org.springframework.stereotype.Repository;

import static org.cmc.curtaincall.domain.lostitem.QLostItem.lostItem;

@Repository
@RequiredArgsConstructor
public class LostItemExistsDao {

    private final JPAQueryFactory query;

    public boolean existsByIdAndCreatedBy(final LostItemId id, final CreatorId createdBy) {
        return query
                .selectOne()
                .from(lostItem)
                .where(
                        lostItem.id.eq(id.getId()),
                        lostItem.createdBy.eq(createdBy),
                        lostItem.useYn.isTrue()
                )
                .fetchFirst() != null;
    }
}
