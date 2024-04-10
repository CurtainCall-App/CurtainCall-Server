package org.cmc.curtaincall.web.show;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.web.show.request.BoxOfficeRequest;
import org.cmc.curtaincall.web.show.response.BoxOfficeResponse;
import org.cmc.curtaincall.web.common.response.ListResult;
import org.cmc.curtaincall.web.common.response.With;
import org.cmc.curtaincall.web.show.response.ShowRank;
import org.cmc.curtaincall.web.show.response.ShowResponse;
import org.cmc.curtaincall.web.show.response.ShowReviewStatsDto;
import org.cmc.curtaincall.web.show.response.ShowReviewStatsResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BoxOfficeController {

    private final BoxOfficeService boxOfficeService;

    private final ShowReviewStatsQueryService showReviewStatsQueryService;

    private final ShowService showService;

    @GetMapping("/box-office")
    public ListResult<With<With<ShowRank, ShowResponse>, ShowReviewStatsResponse>> getList(
            @ModelAttribute @Validated BoxOfficeRequest request
    ) {
        final List<BoxOfficeResponse> boxOfficeResponses = boxOfficeService.getList(request);
        final List<ShowId> showIds = boxOfficeResponses.stream().map(BoxOfficeResponse::id).toList();
        final Map<ShowId, ShowReviewStatsResponse> showIdToStats = showReviewStatsQueryService.getList(showIds)
                .stream()
                .collect(Collectors.toMap(ShowReviewStatsDto::showId, ShowReviewStatsResponse::of));
        final Map<ShowId, ShowResponse> showIdToShow = showService.getList(showIds)
                .stream()
                .collect(Collectors.toMap(ShowResponse::id, Function.identity()));
        return new ListResult<>(boxOfficeResponses.stream()
                .filter(boxOffice -> showIdToShow.containsKey(boxOffice.id()))
                .filter(boxOffice -> showIdToStats.containsKey(boxOffice.id()))
                .map(boxOffice -> new With<>(new With<>(
                        new ShowRank(boxOffice.rank()),
                        showIdToShow.get(boxOffice.id())),
                        showIdToStats.get(boxOffice.id())
                )).toList()
        );
    }
}
