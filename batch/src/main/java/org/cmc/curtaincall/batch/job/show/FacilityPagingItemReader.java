package org.cmc.curtaincall.batch.job.show;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.batch.service.kopis.KopisService;
import org.cmc.curtaincall.batch.service.kopis.request.FacilityListRequest;
import org.cmc.curtaincall.batch.service.kopis.response.FacilityResponse;
import org.springframework.batch.item.database.AbstractPagingItemReader;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiredArgsConstructor
public class FacilityPagingItemReader extends AbstractPagingItemReader<FacilityResponse> {

    private final KopisService kopisService;

    @Override
    protected void doReadPage() {

        FacilityListRequest requestParam = FacilityListRequest.builder()
                .page(getPage() + 1)
                .size(getPageSize())
                .build();
        List<FacilityResponse> facilities = kopisService.getFacilityList(requestParam);

        if (results == null) {
            results = new CopyOnWriteArrayList<>();
        } else {
            results.clear();
        }

        results.addAll(facilities);
    }
}
