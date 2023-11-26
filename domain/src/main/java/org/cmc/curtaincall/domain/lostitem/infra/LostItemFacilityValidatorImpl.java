package org.cmc.curtaincall.domain.lostitem.infra;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.lostitem.validation.LostItemFacilityValidator;
import org.cmc.curtaincall.domain.show.FacilityId;
import org.cmc.curtaincall.domain.show.dao.FacilityExistsDao;
import org.cmc.curtaincall.domain.show.exception.FacilityNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LostItemFacilityValidatorImpl implements LostItemFacilityValidator {

    private final FacilityExistsDao facilityExistsDao;

    @Override
    public void validate(FacilityId facilityId) {
        if (!facilityExistsDao.exists(facilityId)) {
            throw new FacilityNotFoundException(facilityId);
        }
    }
}
