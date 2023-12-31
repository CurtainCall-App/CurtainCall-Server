package org.cmc.curtaincall.domain.member.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.image.Image;
import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.member.exception.MemberNotFoundException;
import org.cmc.curtaincall.domain.member.response.MemberDetailResponse;
import org.cmc.curtaincall.domain.party.PartyMemberRole;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.cmc.curtaincall.domain.member.QMember.member;
import static org.cmc.curtaincall.domain.party.QPartyMember.partyMember;

@Repository
@RequiredArgsConstructor
public class MemberDao {

    private final JPAQueryFactory query;

    public MemberDetailResponse getDetail(final MemberId id) {
        final Member memberEntity = Optional.ofNullable(query
                .selectFrom(member)
                .leftJoin(member.image).fetchJoin()
                .where(
                        member.id.eq(id.getId()),
                        member.useYn.isTrue()
                )
                .fetchOne()
        ).orElseThrow(() -> new MemberNotFoundException(id));

        Long recruitingNum = query.select(partyMember.count())
                .from(partyMember)
                .join(partyMember.party)
                .where(
                        partyMember.role.eq(PartyMemberRole.RECRUITER),
                        partyMember.memberId.eq(id),
                        partyMember.party.useYn.isTrue()
                )
                .fetchOne();
        Long participationNum = query.select(partyMember.count())
                .from(partyMember)
                .where(
                        partyMember.role.eq(PartyMemberRole.PARTICIPANT),
                        partyMember.memberId.eq(id),
                        partyMember.party.useYn.isTrue()
                )
                .fetchOne();

        return MemberDetailResponse.builder()
                .id(id)
                .nickname(memberEntity.getNickname())
                .imageId(Optional.ofNullable(memberEntity.getImage()).map(Image::getId).orElse(null))
                .imageUrl(Optional.ofNullable(memberEntity.getImage()).map(Image::getUrl).orElse(null))
                .recruitingNum(recruitingNum)
                .participationNum(participationNum)
                .build();
    }
}
