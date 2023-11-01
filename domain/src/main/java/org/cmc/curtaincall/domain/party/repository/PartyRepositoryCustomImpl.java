package org.cmc.curtaincall.domain.party.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.party.Party;
import org.cmc.curtaincall.domain.party.PartyId;

import java.util.Collection;
import java.util.List;

import static org.cmc.curtaincall.domain.party.QParty.party;

@RequiredArgsConstructor
public class PartyRepositoryCustomImpl implements PartyRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<PartyId> findAllIdByCreatedByAndPartyIn(CreatorId createdBy, Collection<Party> ids) {
        return query.select(party.id)
                .from(party)
                .where(
                        party.createdBy.eq(createdBy),
                        party.in(party)
                )
                .fetch()
                .stream()
                .map(PartyId::new)
                .toList();
    }

}
