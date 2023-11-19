package org.cmc.curtaincall.domain.show.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.show.FacilityId;
import org.springframework.stereotype.Repository;

import static org.cmc.curtaincall.domain.show.QFacility.facility;

@Repository
@RequiredArgsConstructor
public class FacilityExistsDao {

    private final JPAQueryFactory query;

    public boolean exists(final FacilityId facilityId) {
        return query
                .selectOne()
                .from(facility)
                .where(
                        facility.id.eq(facilityId.getId()),
                        facility.useYn.isTrue()
                )
                .fetchFirst() != null;
    }
}
