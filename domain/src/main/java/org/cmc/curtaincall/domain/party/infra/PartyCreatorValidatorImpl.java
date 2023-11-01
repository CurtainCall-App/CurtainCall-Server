package org.cmc.curtaincall.domain.party.infra;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.party.PartyCreatorValidator;
import org.cmc.curtaincall.domain.party.PartyId;
import org.cmc.curtaincall.domain.party.dao.PartyExistsDao;
import org.cmc.curtaincall.domain.party.exception.PartyAccessDeniedException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PartyCreatorValidatorImpl implements PartyCreatorValidator {

    private final PartyExistsDao partyExistsDao;

    @Override
    public void validate(final PartyId partyId, final CreatorId creatorId) {
        if (!partyExistsDao.existsByIdAndCreatedBy(partyId, creatorId)) {
            throw new PartyAccessDeniedException(partyId, creatorId.getMemberId(),
                    "작성자에게만 허용된 기능입니다.");
        }
    }
}
