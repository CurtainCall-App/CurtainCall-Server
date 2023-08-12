package org.cmc.curtaincall.domain.party.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.party.Party;
import org.cmc.curtaincall.domain.party.request.PartyQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PartyQueryRepository {

    private final JPAQueryFactory query;

    public Slice<Party> query(Pageable pageable, PartyQueryParam queryParam) {
        return null;
    }
}
