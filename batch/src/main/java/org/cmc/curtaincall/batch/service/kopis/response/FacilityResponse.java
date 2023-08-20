package org.cmc.curtaincall.batch.service.kopis.response;

import jakarta.annotation.Nullable;
import lombok.Builder;

@Builder
public record FacilityResponse(
    String id,
    String name,
    int hallNum,
    String characteristics,
    String sido,
    String gugun,
    @Nullable Integer openingYear
) {
}
