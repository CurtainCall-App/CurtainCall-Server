package org.cmc.curtaincall.domain.party.infra;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.member.dao.MemberExistsDao;
import org.cmc.curtaincall.domain.member.exception.MemberNotFoundException;
import org.cmc.curtaincall.domain.party.validation.PartyMemberIdValidator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PartyMemberIdValidatorImpl implements PartyMemberIdValidator {

    private final MemberExistsDao memberExistsDao;

    @Override
    public void validate(final MemberId memberId) {
        if (!memberExistsDao.exists(memberId)) {
            throw new MemberNotFoundException(memberId);
        }
    }
}
