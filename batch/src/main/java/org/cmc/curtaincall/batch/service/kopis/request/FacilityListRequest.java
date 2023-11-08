package org.cmc.curtaincall.batch.service.kopis.request;

import jakarta.annotation.Nullable;
import lombok.Builder;

@Builder
public record FacilityListRequest(
    int page,
    int size,
    @Nullable String name,
    @Nullable String characteristicsCode,
    @Nullable String signguCode,
    @Nullable String gugunCode
) {
}
