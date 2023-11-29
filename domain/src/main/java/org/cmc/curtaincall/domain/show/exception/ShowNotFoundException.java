package org.cmc.curtaincall.domain.show.exception;

import org.cmc.curtaincall.domain.core.AbstractDomainException;
import org.cmc.curtaincall.domain.show.ShowId;

public class ShowNotFoundException extends AbstractDomainException {

    public ShowNotFoundException(final ShowId showId) {
        super(ShowErrorCode.NOT_FOUND, "Show.id=" + showId);
    }

}
