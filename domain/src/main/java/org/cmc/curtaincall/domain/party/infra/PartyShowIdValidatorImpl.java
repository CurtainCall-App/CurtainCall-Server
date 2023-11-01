package org.cmc.curtaincall.domain.party.infra;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.party.PartyShowIdValidator;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.domain.show.dao.ShowExistsDao;
import org.cmc.curtaincall.domain.show.exception.ShowNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PartyShowIdValidatorImpl implements PartyShowIdValidator {

    private final ShowExistsDao showExistsDao;

    @Override
    public void validate(final ShowId showId) {
        if (!showExistsDao.exists(showId)) {
            throw new ShowNotFoundException(showId);
        }
    }
}
