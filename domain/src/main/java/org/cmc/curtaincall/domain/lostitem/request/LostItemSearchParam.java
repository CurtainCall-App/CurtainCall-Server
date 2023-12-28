package org.cmc.curtaincall.domain.lostitem.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.cmc.curtaincall.domain.show.FacilityId;

public record LostItemSearchParam(
        @NotNull FacilityId facilityId,
        @NotBlank String title
) {
}
