package org.cmc.curtaincall.domain.party.validation;

import org.cmc.curtaincall.domain.show.ShowId;

public interface PartyShowIdValidator {

    void validate(final ShowId showId);
}
