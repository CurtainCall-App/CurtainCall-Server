package org.cmc.curtaincall.domain.show.exception;

import org.cmc.curtaincall.domain.core.AbstractDomainException;
import org.cmc.curtaincall.domain.show.FacilityId;

public class FacilityNotFoundException extends AbstractDomainException {

    public FacilityNotFoundException(final FacilityId facilityId) {
        super(ShowErrorCode.FACILITY_NOT_FOUND, "FacilityId=" + facilityId);
    }

}
