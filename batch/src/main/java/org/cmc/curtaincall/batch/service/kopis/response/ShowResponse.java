package org.cmc.curtaincall.batch.service.kopis.response;

import lombok.Builder;

@Builder
public record ShowResponse(
        String id,                  // mt20id
        String name,                // prfnm
        String genreName,           // genrenm
        String state,               // prfstate
        String startDate,           // prfpdfrom
        String endDate,             // prfpdto
        String poster,              // poster
        String facilityName,        // fcltynm
        String openRun              // openrun
) {
}
