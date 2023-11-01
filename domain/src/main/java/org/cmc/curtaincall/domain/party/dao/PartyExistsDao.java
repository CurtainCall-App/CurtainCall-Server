package org.cmc.curtaincall.domain.party.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.party.PartyId;
import org.springframework.stereotype.Repository;

import static org.cmc.curtaincall.domain.party.QParty.party;

@Repository
@RequiredArgsConstructor
public class PartyExistsDao {

    private final JPAQueryFactory query;

    public boolean existsByIdAndCreatedBy(final PartyId partyId, final CreatorId creatorId) {
        return query
                .selectOne()
                .from(party)
                .where(
                        party.id.eq(partyId.getId()),
                        party.createdBy.eq(creatorId)
                )
                .fetchFirst() != null;
    }
}
