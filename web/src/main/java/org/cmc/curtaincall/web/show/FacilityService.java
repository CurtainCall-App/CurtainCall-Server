package org.cmc.curtaincall.web.show;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.show.Facility;
import org.cmc.curtaincall.domain.show.FacilityId;
import org.cmc.curtaincall.domain.show.exception.FacilityNotFoundException;
import org.cmc.curtaincall.domain.show.repository.FacilityRepository;
import org.cmc.curtaincall.web.show.response.FacilityDetailResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacilityService {

    private final FacilityRepository facilityRepository;

    public FacilityDetailResponse getDetail(final FacilityId id) {
        Facility facility = getFacilityById(id);
        return FacilityDetailResponse.builder()
                .id(facility.getId())
                .name(facility.getName())
                .hallNum(facility.getHallNum())
                .characteristic(facility.getCharacteristics())
                .openingYear(facility.getOpeningYear())
                .seatNum(facility.getOpeningYear())
                .phone(facility.getPhone())
                .homepage(facility.getHomepage())
                .address(facility.getAddress())
                .latitude(facility.getLatitude())
                .longitude(facility.getLongitude())
                .build();
    }

    private Facility getFacilityById(final FacilityId id) {
        return facilityRepository.findById(id)
                .filter(Facility::getUseYn)
                .orElseThrow(() -> new FacilityNotFoundException(id));
    }
}
