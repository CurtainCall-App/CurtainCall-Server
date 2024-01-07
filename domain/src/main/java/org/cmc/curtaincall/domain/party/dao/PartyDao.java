package org.cmc.curtaincall.domain.party.dao;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.common.QuerydslHelper;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.party.PartyId;
import org.cmc.curtaincall.domain.party.PartyMemberRole;
import org.cmc.curtaincall.domain.party.exception.PartyNotFoundException;
import org.cmc.curtaincall.domain.party.request.PartySearchParam;
import org.cmc.curtaincall.domain.party.response.PartyDetailResponse;
import org.cmc.curtaincall.domain.party.response.PartyParticipatedResponse;
import org.cmc.curtaincall.domain.party.response.PartyParticipationResponse;
import org.cmc.curtaincall.domain.party.response.PartyRecruitmentResponse;
import org.cmc.curtaincall.domain.party.response.PartyResponse;
import org.cmc.curtaincall.domain.party.response.QPartyDetailResponse;
import org.cmc.curtaincall.domain.party.response.QPartyParticipationResponse;
import org.cmc.curtaincall.domain.party.response.QPartyRecruitmentResponse;
import org.cmc.curtaincall.domain.party.response.QPartyResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
                        party.curMemberNum,
                        party.maxMemberNum,
                        party.createdAt,
                        party.createdBy,
                        member.nickname,
                        member.image.url,
                        party.showId,
                        show.name,
                        show.poster,
                        party.partyAt,
                        facility.id,
                        facility.name
                ))
                .from(party)
                .join(member).on(party.createdBy.memberId.id.eq(member.id))
                .join(member.image)
                .join(show).on(party.showId.eq(show.id))
                .join(facility).on(show.facility.id.eq(facility.id))
                .where(
                        party.id.eq(partyId.getId()),
                        party.useYn.isTrue()
                )
                .fetchOne()
        ).orElseThrow(() -> new PartyNotFoundException(partyId));
    }

    public List<PartyResponse> getList(final Pageable pageable) {
        return query
                .select(new QPartyResponse(
                        party.id,
                        party.title,
                        party.curMemberNum,
                        party.maxMemberNum,
                        party.createdAt,
                        party.createdBy,
                        member.nickname,
                        member.image.url,
                        party.showId,
                        show.name,
                        show.poster,
                        party.partyAt,
                        facility.id,
                        facility.name
                ))
                .from(party)
                .join(member).on(party.createdBy.memberId.id.eq(member.id))
                .join(member.image)
                .join(show).on(party.showId.eq(show.id))
                .join(facility).on(show.facility.id.eq(facility.id))
                .where(
                        party.useYn.isTrue()
                )
                .orderBy(QuerydslHelper.filterNullOrderByArr(
                        getCreatedAtOrder(pageable)
                ))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    public List<PartyRecruitmentResponse> getRecruitmentList(
            final Pageable pageable, final MemberId memberId
    ) {
        return query
                .select(new QPartyRecruitmentResponse(
                        party.id,
                        party.title,
                        party.content,
                        party.curMemberNum,
                        party.maxMemberNum,
                        party.createdAt,
                        party.showId,
                        show.name,
                        show.poster,
                        party.partyAt,
                        facility.id,
                        facility.name
                ))
                .from(partyMember)
                .join(partyMember.party, party)
                .join(show).on(party.showId.eq(show.id))
                .join(facility).on(show.facility.id.eq(facility.id))
                .where(
                        partyMember.role.eq(PartyMemberRole.RECRUITER),
                        party.createdBy.memberId.eq(memberId),
                        party.useYn.isTrue()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    public List<PartyParticipationResponse> getParticipationList(
            final Pageable pageable, final MemberId memberId
    ) {
        return query
                .select(new QPartyParticipationResponse(
                        party.id,
                        party.title,
                        party.content,
                        party.curMemberNum,
                        party.maxMemberNum,
                        party.createdAt,
                        party.createdBy,
                        member.nickname,
                        member.image.url,
                        party.showId,
                        show.name,
                        show.poster,
                        party.partyAt,
                        facility.id,
                        facility.name
                ))
                .from(partyMember)
                .join(partyMember.party, party)
                .join(member).on(party.createdBy.memberId.id.eq(member.id))
                .join(member.image)
                .join(show).on(party.showId.eq(show.id))
                .join(facility).on(show.facility.id.eq(facility.id))
                .where(
                        partyMember.role.eq(PartyMemberRole.PARTICIPANT),
                        partyMember.memberId.eq(memberId),
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

    public List<PartyResponse> search(final Pageable pageable, final PartySearchParam searchParam) {
        return query
                .select(new QPartyResponse(
                        party.id,
                        party.title,
                        party.curMemberNum,
                        party.maxMemberNum,
                        party.createdAt,
                        party.createdBy,
                        member.nickname,
                        member.image.url,
                        party.showId,
                        show.name,
                        show.poster,
                        party.partyAt,
                        facility.id,
                        facility.name
                ))
                .from(party)
                .join(member).on(party.createdBy.memberId.id.eq(member.id))
                .join(member.image)
                .join(show).on(party.showId.eq(show.id))
                .join(facility).on(show.facility.id.eq(facility.id))
                .where(
                        show.name.startsWith(searchParam.getKeyword())
                                .or(facility.name.startsWith(searchParam.getKeyword())),
                        party.useYn.isTrue()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    public List<PartyParticipatedResponse> areParticipated(final List<PartyId> partyIds, final MemberId memberId) {
        Set<PartyId> participating = query.select(partyMember.party.id)
                .from(partyMember)
                .where(
                        partyMember.memberId.eq(memberId),
                        partyMember.party.id.in(partyIds.stream().map(PartyId::getId).toList())
                )
                .fetch()
                .stream()
                .map(PartyId::new)
                .collect(Collectors.toSet());
        return partyIds.stream()
                .map(partyId -> new PartyParticipatedResponse(partyId, participating.contains(partyId)))
                .toList();
    }
}
