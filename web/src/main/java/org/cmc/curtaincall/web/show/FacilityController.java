package org.cmc.curtaincall.web.show;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.show.FacilityId;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.web.show.response.FacilityDetailResponse;
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

    @GetMapping("/facilities/{id}")
    public FacilityDetailResponse getFacilityDetail(@PathVariable final FacilityId id) {
        return facilityService.getDetail(id);
    }

    @GetMapping("/facilities/{facilityId}/shows")
    public Slice<ShowResponse> getShowListOfFacility(
            Pageable pageable, @PathVariable FacilityId facilityId, @RequestParam(required = false) ShowGenre genre
    ) {
        return showService.getListOfFacility(pageable, facilityId, genre);
    }
}
