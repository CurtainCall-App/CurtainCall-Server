package org.cmc.curtaincall.batch.service.kopis.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ShowDetailResponse(
        String id,                  // mt20id
        String name,                // prfnm
        String startDate,           // prfpdfrom
        String endDate,             // prfpdto
        String facilityName,        // fcltynm
        String facilityId,           // mt10id
        String cast,                // prfcast
        String crew,                // prfcrew
        String runtime,             // prfruntime
        String age,                 // prfage
        String enterprise,          // entrpsnm
        String price,               // pcseguidance
        String poster,              // poster
        String story,               // sty
        String genreName,           // genrenm
        String state,               // prfstate
        String openRun,              // openrun
        String showTimes,            // dtguidance
        List<String> introductionImages     // styurls
) {
}
