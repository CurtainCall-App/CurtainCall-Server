package org.cmc.curtaincall.domain.lostitem.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.lostitem.LostItem;
import org.cmc.curtaincall.domain.lostitem.LostItemType;
import org.cmc.curtaincall.domain.lostitem.request.LostItemQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.cmc.curtaincall.domain.lostitem.QLostItem.lostItem;

@Repository
@RequiredArgsConstructor
public class LostItemQueryRepository {

    private final JPAQueryFactory query;

    public Slice<LostItem> query(Pageable pageable, LostItemQueryParam queryParam) {
        JPAQuery<LostItem> contentQuery = query
                .selectFrom(lostItem)
                .join(lostItem.facility).fetchJoin()
                .join(lostItem.image).fetchJoin()
                .where(
                        facilityIdEq(queryParam.getFacilityId()),
                        typeEq(queryParam.getType()),
                        foundDateEq(queryParam.getFoundDate()),
                        titleStartsWith(queryParam.getTitle()),
                        lostItem.useYn.isTrue()
                );
        if (queryParam.getTitle() != null) {
            contentQuery.orderBy(lostItem.foundDate.desc(), lostItem.foundTime.desc());
        }
        List<LostItem> content = contentQuery
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

    private BooleanExpression foundDateEq(LocalDate foundDate) {
        return Optional.ofNullable(foundDate)
                .map(lostItem.foundDate::eq)
                .orElse(null);
    }

}
