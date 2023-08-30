package org.cmc.curtaincall.batch.job.show;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.batch.service.kopis.KopisService;
import org.cmc.curtaincall.batch.service.kopis.request.ShowListRequest;
import org.cmc.curtaincall.batch.service.kopis.response.ShowResponse;
import org.springframework.batch.item.database.AbstractPagingItemReader;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiredArgsConstructor
public class ShowPagingItemReader extends AbstractPagingItemReader<ShowResponse> {

    private final KopisService kopisService;

    private final LocalDate startDate;

    private final LocalDate endDate;

    @Override
    protected void doReadPage() {
        ShowListRequest requestParam = ShowListRequest.builder()
                .page(getPage() + 1)
                .size(getPageSize())
                .startDate(startDate)
                .endDate(endDate)
                .build();

        List<ShowResponse> facilities = kopisService.getShowList(requestParam);

        if (results == null) {
            results = new CopyOnWriteArrayList<>();
        } else {
            results.clear();
        }

        results.addAll(facilities);
    }
}
