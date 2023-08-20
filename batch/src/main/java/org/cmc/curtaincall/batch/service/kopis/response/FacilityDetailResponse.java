package org.cmc.curtaincall.batch.service.kopis.response;

import jakarta.annotation.Nullable;
import lombok.Builder;

@Builder
public record FacilityDetailResponse(
    String id,
    String name,
    int hallNum,
    String characteristics,
    @Nullable Integer openingYear,
    int seatNum,
    String phone,
    String homepage,
    String address,
    double latitude,
    double longitude
) {
}
