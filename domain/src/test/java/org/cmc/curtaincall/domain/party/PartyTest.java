package org.cmc.curtaincall.domain.party;

import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.show.ShowId;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class PartyTest {

//    @Test
    void participate() {
        // given
        LocalDateTime showAt = mock(LocalDateTime.class);
        given(showAt.isBefore(any())).willReturn(true);

        Party party = Party.builder()
                .showId(new ShowId("show-id"))
                .showAt(showAt)
                .title("title")
                .content("content")
                .maxMemberNum(10)
                .category(PartyCategory.WATCHING)
                .build();

        PartyMemberIdValidator partyMemberIdValidator = mock(PartyMemberIdValidator.class);

        // when
        party.participate(new MemberId(20L), partyMemberIdValidator);

        // then
        assertThat(party.getCurMemberNum()).isEqualTo(2);
        assertThat(party.getPartyMembers().stream().map(PartyMember::getMemberId))
                .containsExactly(new MemberId(20L));
    }
}