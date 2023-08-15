package org.cmc.curtaincall.domain.party.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.party.Party;
import org.cmc.curtaincall.domain.party.request.PartySearchParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.cmc.curtaincall.domain.party.QParty.party;
import static org.cmc.curtaincall.domain.show.QFacility.facility;
import static org.cmc.curtaincall.domain.show.QShow.show;

@Repository
@RequiredArgsConstructor
public class PartyQueryRepository {

    private final JPAQueryFactory query;

    public Slice<Party> search(Pageable pageable, PartySearchParam searchParam) {
        List<Party> content = query
                .selectFrom(party)
                .join(party.createdBy).fetchJoin()
                .join(party.createdBy.image).fetchJoin()
                .join(party.show, show).fetchJoin()
                .join(show.facility, facility).fetchJoin()
                .where(
                        show.name.startsWith(searchParam.getKeyword())
                                .or(facility.name.startsWith(searchParam.getKeyword())),
                        party.category.eq(searchParam.getCategory()),
                        party.useYn.isTrue()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1L)
                .fetch();

        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }
}
