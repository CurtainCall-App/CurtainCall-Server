package org.cmc.curtaincall.domain.lostitem.dao;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.common.QuerydslHelper;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.lostitem.LostItemId;
import org.cmc.curtaincall.domain.lostitem.exception.LostItemNotFoundException;
import org.cmc.curtaincall.domain.lostitem.request.LostItemListQueryParam;
import org.cmc.curtaincall.domain.lostitem.response.*;
import org.cmc.curtaincall.domain.show.FacilityId;
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

    public List<LostItemResponse> getList(final Pageable pageable, final LostItemListQueryParam queryParam) {
        JPAQuery<LostItemResponse> contentQuery = query
                .select(new QLostItemResponse(
                        lostItem.id,
                        lostItem.facilityId,
                        facility.name,
                        lostItem.title,
                        lostItem.foundDate,
                        lostItem.foundTime,
                        lostItem.image.url,
                        lostItem.createdAt
                ))
                .from(lostItem)
                .join(facility).on(lostItem.facilityId.eq(facility.id))
                .join(lostItem.image)
                .where(
                        facilityIdEq(queryParam.facilityId()),
                        foundDateGoe(queryParam.foundDateStart()),
                        foundDateLoe(queryParam.foundDateEnd()),
                        lostItem.useYn.isTrue()
                );

        if (queryParam.foundDateStart() == null && queryParam.foundDateEnd() == null) {
            final OrderSpecifier<LocalDateTime> createdAtOrderSpecifier = Optional.ofNullable(pageable.getSort().getOrderFor("createdAt"))
                    .map(order -> Order.valueOf(order.getDirection().name()))
                    .map(order -> new OrderSpecifier<>(order, lostItem.createdAt))
                    .orElse(null);
            contentQuery.orderBy(QuerydslHelper.filterNullOrderByArr(createdAtOrderSpecifier));
        }
        
        return contentQuery
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression facilityIdEq(final FacilityId facilityId) {
        return Optional.ofNullable(facilityId)
                .map(lostItem.facilityId::eq)
                .orElse(null);
    }

    private BooleanExpression foundDateGoe(final LocalDate date) {
        return Optional.ofNullable(date)
                .map(lostItem.foundDate::goe)
                .orElse(null);
    }

    private BooleanExpression foundDateLoe(final LocalDate date) {
        return Optional.ofNullable(date)
                .map(lostItem.foundDate::loe)
                .orElse(null);
    }

    public LostItemDetailResponse getDetail(final LostItemId lostItemId) {
        return Optional.ofNullable(query
                .select(new QLostItemDetailResponse(
                        lostItem.id,
                        lostItem.facilityId,
                        facility.name,
                        facility.phone,
                        lostItem.title,
                        lostItem.foundPlaceDetail,
                        lostItem.foundDate,
                        lostItem.foundTime,
                        lostItem.particulars,
                        lostItem.image.id,
                        lostItem.image.url,
                        lostItem.createdAt
                ))
                .from(lostItem)
                .join(facility).on(lostItem.facilityId.eq(facility.id))
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
                        lostItem.facilityId,
                        facility.name,
                        lostItem.title,
                        lostItem.foundDate,
                        lostItem.foundTime,
                        lostItem.image.url,
                        lostItem.createdAt
                ))
                .from(lostItem)
                .join(facility).on(lostItem.facilityId.eq(facility.id))
                .join(lostItem.image).fetchJoin()
                .where(
                        lostItem.createdBy.eq(createdBy),
                        lostItem.useYn.isTrue()
                )
                .orderBy(QuerydslHelper.filterNullOrderByArr(getCreatedAtOrder(pageable)))
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
