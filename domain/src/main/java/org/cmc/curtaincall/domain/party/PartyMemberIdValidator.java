package org.cmc.curtaincall.domain.party;

import org.cmc.curtaincall.domain.member.MemberId;

public interface PartyMemberIdValidator {

    void validate(final MemberId memberId);
}
