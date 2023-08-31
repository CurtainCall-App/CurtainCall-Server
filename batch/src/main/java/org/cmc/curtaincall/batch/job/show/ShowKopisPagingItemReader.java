package org.cmc.curtaincall.batch.job.show;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.batch.job.common.WithPresent;
import org.cmc.curtaincall.batch.service.kopis.KopisService;
import org.cmc.curtaincall.batch.service.kopis.request.ShowListRequest;
import org.cmc.curtaincall.batch.service.kopis.response.ShowResponse;
import org.springframework.batch.item.database.AbstractPagingItemReader;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiredArgsConstructor
public class ShowKopisPagingItemReader extends AbstractPagingItemReader<WithPresent<ShowResponse>> {

    private final KopisService kopisService;

    private final LocalDate startDate;

    private final LocalDate endDate;

    private final EntityManager em;


    @Override
    protected void doReadPage() {
        ShowListRequest requestParam = ShowListRequest.builder()
                .page(getPage() + 1)
                .size(getPageSize())
                .startDate(startDate)
                .endDate(endDate)
                .build();

        List<ShowResponse> showListResponse = kopisService.getShowList(requestParam);

        List<String> showListResponseIdList = showListResponse.stream()
                .map(ShowResponse::id)
                .toList();

        Set<String> existenceIds = new HashSet<>(em.createQuery("""
                            select show.id
                            from Show show
                            where show.id in :ids
                        """, String.class)
                .setParameter("ids", showListResponseIdList)
                .getResultList());


        if (results == null) {
            results = new CopyOnWriteArrayList<>();
        } else {
            results.clear();
        }

        results.addAll(showListResponse.stream()
                .map(show -> new WithPresent<>(show, existenceIds.contains(show.id())))
                .toList()
        );
    }
}
