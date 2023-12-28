package org.cmc.curtaincall.domain.lostitem.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.cmc.curtaincall.domain.show.FacilityId;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class LostItemQueryParam {

    private FacilityId facilityId;

    private LocalDate foundDate;

    private String title;
}
