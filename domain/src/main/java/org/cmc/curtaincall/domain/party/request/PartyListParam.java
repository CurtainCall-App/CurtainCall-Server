package org.cmc.curtaincall.domain.party.request;

import jakarta.annotation.Nullable;

import java.time.LocalDate;

public record PartyListParam(
        @Nullable LocalDate startDate,
        @Nullable LocalDate endDate
) {
}
