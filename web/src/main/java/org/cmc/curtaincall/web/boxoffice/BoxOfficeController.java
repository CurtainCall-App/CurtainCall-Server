package org.cmc.curtaincall.web.boxoffice;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.web.boxoffice.dto.BoxOfficeRequest;
import org.cmc.curtaincall.web.boxoffice.dto.BoxOfficeResponse;
import org.cmc.curtaincall.web.common.response.ListResult;
import org.cmc.curtaincall.web.common.response.With;
import org.cmc.curtaincall.web.show.ShowReviewStatsQueryService;
import org.cmc.curtaincall.web.show.response.ShowReviewStatsDto;
import org.cmc.curtaincall.web.show.response.ShowReviewStatsResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BoxOfficeController {

    private final BoxOfficeService boxOfficeService;

    private final ShowReviewStatsQueryService showReviewStatsQueryService;

    @GetMapping("/box-office")
    public ListResult<With<BoxOfficeResponse, ShowReviewStatsResponse>> getList(@ModelAttribute @Validated BoxOfficeRequest request) {
        final List<BoxOfficeResponse> boxOfficeResponses = boxOfficeService.getList(request);
        final List<ShowId> showIds = boxOfficeResponses.stream().map(BoxOfficeResponse::id).toList();
        final Map<ShowId, ShowReviewStatsResponse> showIdToStats = showReviewStatsQueryService.getList(showIds)
                .stream()
                .collect(Collectors.toMap(ShowReviewStatsDto::showId, ShowReviewStatsResponse::of));
        return new ListResult<>(boxOfficeResponses.stream()
                .map(show -> new With<>(
                        show, showIdToStats.getOrDefault(show.id(), new ShowReviewStatsResponse(0, 0L, 0D)
                ))).toList()
        );
    }
}
