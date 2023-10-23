package org.cmc.curtaincall.domain.show.exception;

import org.cmc.curtaincall.domain.common.DomainErrorCode;
import org.cmc.curtaincall.domain.common.DomainException;
import org.cmc.curtaincall.domain.show.ShowId;

public class ShowNotFoundException extends DomainException {

    public ShowNotFoundException(ShowId showId) {
        super(DomainErrorCode.NOT_FOUND, "Show.id=" + showId);
    }

    @Override
    public String getExternalMessage() {
        return "존재하지 않는 공연";
    }
}
