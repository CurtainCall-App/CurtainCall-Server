package org.cmc.curtaincall.domain.show.exception;

import org.cmc.curtaincall.domain.core.DomainErrorCode;
import org.cmc.curtaincall.domain.core.DomainException;
import org.cmc.curtaincall.domain.show.FacilityId;

public class FacilityNotFoundException extends DomainException {

    public FacilityNotFoundException(final FacilityId facilityId) {
        super(DomainErrorCode.NOT_FOUND, "FacilityId=" + facilityId);
    }

    @Override
    public String getExternalMessage() {
        return "존재하지 않는 공연장입니다.";
    }
}
