package org.cmc.curtaincall.batch.service.kopis.request;

import jakarta.annotation.Nullable;
import lombok.Builder;
import org.cmc.curtaincall.domain.show.ShowGenre;

import java.time.LocalDate;

@Builder
public record ShowListRequest(
        int page,                               // cpage
        int size,                               // rows
        LocalDate startDate,                    // stdate
        LocalDate endDate,                      // eddate
        @Nullable ShowGenre genre,            // shcate
        @Nullable String name,                // shprfnm
        @Nullable String facilityName,        // shprfnmfct
        @Nullable String facilityCode,        // prfplccd
        @Nullable String sido,                // sigugucode
        @Nullable String gugun,               // sigugucodesub
        @Nullable String kid,                 // kidstate
        @Nullable String state,               // prfstate
        @Nullable String openRun              // openrun
) {
}
