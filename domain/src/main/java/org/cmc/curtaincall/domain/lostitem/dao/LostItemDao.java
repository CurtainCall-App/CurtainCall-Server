package org.cmc.curtaincall.domain.lostitem.dao;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.common.RepositoryHelper;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.lostitem.LostItemId;
import org.cmc.curtaincall.domain.lostitem.LostItemType;
import org.cmc.curtaincall.domain.lostitem.exception.LostItemNotFoundException;
import org.cmc.curtaincall.domain.lostitem.request.LostItemQueryParam;
import org.cmc.curtaincall.domain.lostitem.response.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.cmc.curtaincall.domain.lostitem.QLostItem.lostItem;
import static org.cmc.curtaincall.domain.show.QFacility.facility;

@Repository
@RequiredArgsConstructor
public class LostItemDao {

    private final JPAQueryFactory query;

    public List<LostItemResponse> search(final Pageable pageable, final LostItemQueryParam queryParam) {
        JPAQuery<LostItemResponse> contentQuery = query
                .select(new QLostItemResponse(
                        lostItem.id,
                        facility.id,
                        facility.name,
                        lostItem.title,
                        lostItem.type,
                        lostItem.foundDate,
                        lostItem.foundTime,
                        lostItem.image.url,
                        lostItem.createdAt
                ))
                .from(lostItem)
                .join(facility).on(lostItem.facilityId.id.eq(facility.id))
                .join(lostItem.image)
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
        return contentQuery
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression titleStartsWith(String title) {
        return Optional.ofNullable(title)
                .map(lostItem.title::startsWith)
                .orElse(null);
    }

    private BooleanExpression facilityIdEq(String facilityId) {
        return Optional.ofNullable(facilityId)
                .map(lostItem.facilityId.id::eq)
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

    public LostItemDetailResponse getDetail(final LostItemId lostItemId) {
        return Optional.ofNullable(query
                .select(new QLostItemDetailResponse(
                        lostItem.id,
                        facility.id,
                        facility.name,
                        facility.phone,
                        lostItem.title,
                        lostItem.type,
                        lostItem.foundPlaceDetail,
                        lostItem.foundDate,
                        lostItem.foundTime,
                        lostItem.particulars,
                        lostItem.image.id,
                        lostItem.image.url,
                        lostItem.createdAt
                ))
                .from(lostItem)
                .join(facility).on(lostItem.facilityId.id.eq(facility.id))
                .join(lostItem.image).fetchJoin()
                .where(
                        lostItem.id.eq(lostItemId.getId()),
                        lostItem.useYn.isTrue()
                )
                .fetchOne()
        ).orElseThrow(() -> new LostItemNotFoundException(lostItemId));
    }

    public List<LostItemMyResponse> getMyList(final Pageable pageable, final CreatorId createdBy) {
        return query
                .select(new QLostItemMyResponse(
                    lostItem.id,
                        facility.id,
                        facility.name,
                        lostItem.title,
                        lostItem.type,
                        lostItem.foundDate,
                        lostItem.foundTime,
                        lostItem.image.url,
                        lostItem.createdAt
                ))
                .from(lostItem)
                .join(facility).on(lostItem.facilityId.id.eq(facility.id))
                .join(lostItem.image).fetchJoin()
                .where(
                        lostItem.createdBy.eq(createdBy),
                        lostItem.useYn.isTrue()
                )
                .orderBy(RepositoryHelper.filterNullOrderByArr(getCreatedAtOrder(pageable)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private OrderSpecifier<LocalDateTime> getCreatedAtOrder(Pageable pageable) {
        Sort.Order sortOrder = pageable.getSort().getOrderFor("createdAt");
        if (sortOrder == null) {
            return null;
        }
        Order order = Order.valueOf(sortOrder.getDirection().name());
        return new OrderSpecifier<>(order, lostItem.createdAt);
    }
}
