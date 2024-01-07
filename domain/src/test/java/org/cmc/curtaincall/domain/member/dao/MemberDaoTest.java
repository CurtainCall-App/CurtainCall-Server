package org.cmc.curtaincall.domain.member.dao;

import org.cmc.curtaincall.domain.common.AbstractDataJpaTest;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.member.response.MemberDetailResponse;
import org.cmc.curtaincall.domain.party.Party;
import org.cmc.curtaincall.domain.show.ShowId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Import(MemberDao.class)
class MemberDaoTest extends AbstractDataJpaTest {

    @Autowired
    private MemberDao memberDao;

    @Test
    void getDetail() {
        // given
        final Member member = Member.builder()
                .nickname("nickname")
                .build();
        em.persist(member);

        final Party party = Party.builder()
                .showId(new ShowId("show-id"))
                .partyAt(LocalDateTime.of(2023, 11, 9, 23, 53))
                .title("title")
                .content("content")
                .maxMemberNum(5)
                .createdBy(new CreatorId(new MemberId(member.getId())))
                .build();
        em.persist(party);

        // when
        final MemberDetailResponse result = memberDao.getDetail(new MemberId(member.getId()));

        // then
        assertThat(result.getId()).isEqualTo(new MemberId(member.getId()));
        assertThat(result.getNickname()).isEqualTo("nickname");
        assertThat(result.getImageId()).isNull();
        assertThat(result.getImageUrl()).isNull();
        assertThat(result.getRecruitingNum()).isEqualTo(1L);
        assertThat(result.getParticipationNum()).isZero();
    }

    @Test
    void getDetail_given_recruiting_party_delete_then_recruiting_num_zero() {
        // given
        final Member member = Member.builder()
                .nickname("nickname")
                .build();
        em.persist(member);

        final Party party = Party.builder()
                .showId(new ShowId("show-id"))
                .partyAt(LocalDateTime.of(2023, 11, 9, 23, 53))
                .title("title")
                .content("content")
                .maxMemberNum(5)
                .createdBy(new CreatorId(new MemberId(member.getId())))
                .build();
        em.persist(party);
        party.delete();

        // when
        final MemberDetailResponse result = memberDao.getDetail(new MemberId(member.getId()));

        // then
        assertThat(result.getRecruitingNum()).isZero();
    }
}