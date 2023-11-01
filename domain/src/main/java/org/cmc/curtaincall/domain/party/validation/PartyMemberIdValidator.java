package org.cmc.curtaincall.domain.party.validation;

import org.cmc.curtaincall.domain.member.MemberId;

public interface PartyMemberIdValidator {

    void validate(final MemberId memberId);
}
