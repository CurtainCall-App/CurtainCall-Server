package org.cmc.curtaincall.batch.service.kopis.request;

import lombok.Builder;

@Builder
public record FacilityListRequest(
    int page,
    int size,
    Integer name,
    Integer characteristicsCode,
    Integer signguCode,
    Integer gugunCode
) {
}
