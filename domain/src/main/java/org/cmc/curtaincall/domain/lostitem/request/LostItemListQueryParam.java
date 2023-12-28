package org.cmc.curtaincall.domain.lostitem.request;

import jakarta.validation.constraints.NotNull;
import org.cmc.curtaincall.domain.show.FacilityId;

import java.time.LocalDate;

public record LostItemListQueryParam(
        @NotNull FacilityId facilityId,
        LocalDate foundDateStart,
        LocalDate foundDateEnd
) {
}
