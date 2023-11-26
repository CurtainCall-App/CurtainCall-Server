package org.cmc.curtaincall.web.show;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.show.FacilityId;
import org.cmc.curtaincall.web.show.response.FacilityDetailResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FacilityController {

    private final FacilityService facilityService;

    private final ShowService showService;

    @GetMapping("/facilities/{id}")
    public FacilityDetailResponse getFacilityDetail(@PathVariable final FacilityId id) {
        return facilityService.getDetail(id);
    }

}
