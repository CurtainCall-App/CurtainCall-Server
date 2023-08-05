package org.cmc.curtaincall.web.controller;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.web.service.facility.FacilityService;
import org.cmc.curtaincall.web.service.facility.response.FacilityDetailResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FacilityController {

    private final FacilityService facilityService;

    @GetMapping("/facilities/{facilityId}")
    public FacilityDetailResponse getFacilityDetail(@PathVariable String facilityId) {
        return facilityService.getDetail(facilityId);
    }
}
