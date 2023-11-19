package org.cmc.curtaincall.web.show;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.web.show.FacilityService;
import org.cmc.curtaincall.web.show.response.FacilityDetailResponse;
import org.cmc.curtaincall.web.show.ShowService;
import org.cmc.curtaincall.web.show.response.ShowResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FacilityController {

    private final FacilityService facilityService;

    private final ShowService showService;

    @GetMapping("/facilities/{facilityId}")
    public FacilityDetailResponse getFacilityDetail(@PathVariable String facilityId) {
        return facilityService.getDetail(facilityId);
    }

    @GetMapping("/facilities/{facilityId}/shows")
    public Slice<ShowResponse> getShowListOfFacility(
            Pageable pageable, @PathVariable String facilityId, @RequestParam(required = false) ShowGenre genre
    ) {
        return showService.getListOfFacility(pageable, facilityId, genre);
    }
}
