package org.cmc.curtaincall.domain.party.dao;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.common.RepositoryHelper;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.party.PartyCategory;
import org.cmc.curtaincall.domain.party.PartyId;
import org.cmc.curtaincall.domain.party.exception.PartyNotFoundException;
import org.cmc.curtaincall.domain.party.request.PartySearchParam;
import org.cmc.curtaincall.domain.party.response.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.cmc.curtaincall.domain.member.QMember.member;
import static org.cmc.curtaincall.domain.party.QParty.party;
import static org.cmc.curtaincall.domain.party.QPartyMember.partyMember;
import static org.cmc.curtaincall.domain.show.QFacility.facility;
import static org.cmc.curtaincall.domain.show.QShow.show;

@Repository
@RequiredArgsConstructor
public class PartyDao {

    private final JPAQueryFactory query;

    public PartyDetailResponse getDetail(PartyId partyId) {
        return Optional.ofNullable(query.select(new QPartyDetailResponse(
                        party.id,
                        party.title,
                        party.content,
                        party.category,
                        party.curMemberNum,
                        party.maxMemberNum,
                        party.createdAt,
                        party.createdBy.memberId.id,
                        member.nickname,
                        member.image.url,
                        show.id,
                        show.name,
                        show.poster,
                        party.showAt,
                        facility.id,
                        facility.name
                ))
                .from(party)
                .join(member).on(party.createdBy.memberId.id.eq(member.id))
                .join(member.image)
                .join(show).on(party.show.id.eq(show.id))
                .join(facility).on(show.facility.id.eq(facility.id))
                .where(party.id.eq(partyId.getId()))
                .fetchOne()
        ).orElseThrow(() -> new PartyNotFoundException(partyId));
    }

    public List<PartyResponse> getList(Pageable pageable, PartyCategory category) {
        return query
                .select(new QPartyResponse(
                        party.id,
                        party.title,
                        party.curMemberNum,
                        party.maxMemberNum,
                        party.createdAt,
                        party.category,
                        party.createdBy.memberId.id,
                        member.nickname,
                        member.image.url,
                        show.id,
                        show.name,
                        show.poster,
                        party.showAt,
                        facility.id,
                        facility.name
                ))
                .from(party)
                .join(member).on(party.createdBy.memberId.id.eq(member.id))
                .join(member.image)
                .join(show).on(party.show.id.eq(show.id))
                .join(facility).on(show.facility.id.eq(facility.id))
                .where(
                        party.category.eq(category),
                        party.useYn.isTrue()
                )
                .orderBy(RepositoryHelper.filterNullOrderByArr(
                        getCreatedAtOrder(pageable)
                ))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    public List<PartyRecruitmentResponse> getRecruitmentList(
            Pageable pageable, MemberId memberId, @Nullable PartyCategory category
    ) {
        return query
                .select(new QPartyRecruitmentResponse(
                        party.id,
                        party.title,
                        party.content,
                        party.curMemberNum,
                        party.maxMemberNum,
                        party.createdAt,
                        party.category,
                        show.id,
                        show.name,
                        show.poster,
                        party.showAt,
                        facility.id,
                        facility.name
                ))
                .from(party)
                .join(show).on(party.show.id.eq(show.id))
                .join(facility).on(show.facility.id.eq(facility.id))
                .where(
                        partyCategoryEq(category),
                        party.createdBy.memberId.eq(memberId),
                        party.useYn.isTrue()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    public List<PartyParticipationResponse> getParticipationList(
            Pageable pageable, MemberId memberId, @Nullable PartyCategory category
    ) {
        return query
                .select(new QPartyParticipationResponse(
                        party.id,
                        party.title,
                        party.content,
                        party.curMemberNum,
                        party.maxMemberNum,
                        party.createdAt,
                        party.category,
                        party.createdBy.memberId.id,
                        member.nickname,
                        member.image.url,
                        show.id,
                        show.name,
                        show.poster,
                        party.showAt,
                        facility.id,
                        facility.name
                ))
                .from(partyMember)
                .join(partyMember.party, party)
                .join(member).on(party.createdBy.memberId.id.eq(member.id))
                .join(member.image)
                .join(show).on(party.show.id.eq(show.id))
                .join(facility).on(show.facility.id.eq(facility.id))
                .where(
                        partyMember.member.id.eq(memberId.getId()),
                        partyCategoryEq(category),
                        party.useYn.isTrue()
                )
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
        return new OrderSpecifier<>(order, party.createdAt);
    }

    private BooleanExpression partyCategoryEq(@Nullable PartyCategory category) {
        return Optional.ofNullable(category)
                .map(party.category::eq)
                .orElse(null);
    }

    public List<PartyResponse> search(Pageable pageable, PartySearchParam searchParam) {
        return query
                .select(new QPartyResponse(
                        party.id,
                        party.title,
                        party.curMemberNum,
                        party.maxMemberNum,
                        party.createdAt,
                        party.category,
                        party.createdBy.memberId.id,
                        member.nickname,
                        member.image.url,
                        show.id,
                        show.name,
                        show.poster,
                        party.showAt,
                        facility.id,
                        facility.name
                ))
                .from(party)
                .join(member).on(party.createdBy.memberId.id.eq(member.id))
                .join(member.image)
                .join(show).on(party.show.id.eq(show.id))
                .join(facility).on(show.facility.id.eq(facility.id))
                .where(
                        show.name.startsWith(searchParam.getKeyword())
                                .or(facility.name.startsWith(searchParam.getKeyword())),
                        party.category.eq(searchParam.getCategory()),
                        party.useYn.isTrue()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
