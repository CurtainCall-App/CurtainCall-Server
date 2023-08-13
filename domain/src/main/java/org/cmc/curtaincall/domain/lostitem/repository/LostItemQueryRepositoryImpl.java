package org.cmc.curtaincall.domain.lostitem.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.lostitem.LostItem;
import org.cmc.curtaincall.domain.lostitem.LostItemType;
import org.cmc.curtaincall.domain.lostitem.request.LostItemQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.cmc.curtaincall.domain.lostitem.QLostItem.lostItem;

@Repository
@RequiredArgsConstructor
public class LostItemQueryRepositoryImpl implements LostItemQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Slice<LostItem> query(Pageable pageable, LostItemQueryParam queryParam) {
        List<LostItem> content = query
                .selectFrom(lostItem)
                .join(lostItem.facility).fetchJoin()
                .join(lostItem.image).fetchJoin()
                .where(
                        titleStartsWith(queryParam.getTitle()),
                        facilityIdEq(queryParam.getFacilityId()),
                        typeEq(queryParam.getType()),
                        lostItem.useYn.isTrue()
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

    private BooleanExpression titleStartsWith(String title) {
        return Optional.ofNullable(title)
                .map(lostItem.title::startsWith)
                .orElse(null);
    }

    private BooleanExpression facilityIdEq(String facilityId) {
        return Optional.ofNullable(facilityId)
                .map(lostItem.facility.id::eq)
                .orElse(null);
    }

    private BooleanExpression typeEq(LostItemType type) {
        return Optional.ofNullable(type)
                .map(lostItem.type::eq)
                .orElse(null);
    }

}
