package org.cmc.curtaincall.web.show;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.show.FacilityId;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.web.common.response.ListResult;
import org.cmc.curtaincall.web.common.response.With;
import org.cmc.curtaincall.web.show.request.ShowListRequest;
import org.cmc.curtaincall.web.show.response.ShowDetailResponse;
import org.cmc.curtaincall.web.show.response.ShowResponse;
import org.cmc.curtaincall.web.show.response.ShowReviewStatsDto;
import org.cmc.curtaincall.web.show.response.ShowReviewStatsResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ShowController {

    private final ShowService showService;

    private final ShowReviewStatsQueryService showReviewStatsQueryService;

    @GetMapping("/shows")
    public ListResult<With<ShowResponse, ShowReviewStatsResponse>> getShows(
            @Validated @ModelAttribute final ShowListRequest showListRequest,
            final Pageable pageable
    ) {
        return getShowWithReviewStatsListResult(showService.getList(showListRequest, pageable));
    }

    @GetMapping("/shows/{showId}")
    public With<ShowDetailResponse, ShowReviewStatsResponse> getShowDetail(@PathVariable final ShowId showId) {
        final ShowDetailResponse showDetailResponse = showService.getDetail(showId);
        final ShowReviewStatsDto showReviewStatsDto = showReviewStatsQueryService.get(showId);
        return new With<>(showDetailResponse, ShowReviewStatsResponse.of(showReviewStatsDto));
    }

    @GetMapping("/shows-to-open")
    public ListResult<With<ShowResponse, ShowReviewStatsResponse>> getShowListToOpen(
            @SortDefault(sort = "startDate") final Pageable pageable,
            @RequestParam final LocalDate startDate) {
        return getShowWithReviewStatsListResult(showService.getListToOpen(pageable, startDate));
    }

    @GetMapping("/shows-to-end")
    public ListResult<With<ShowResponse, ShowReviewStatsResponse>> getShowListToEnd(
            @SortDefault(sort = "endDate") final Pageable pageable,
            @RequestParam final LocalDate endDate,
            @RequestParam(required = false) final ShowGenre genre
    ) {
        return getShowWithReviewStatsListResult(showService.getListToEnd(pageable, endDate, genre));
    }

    @GetMapping("/search/shows")
    public ListResult<With<ShowResponse, ShowReviewStatsResponse>> searchShows(
            final Pageable pageable, @RequestParam @Validated @Size(max = 100) @NotBlank final String keyword
    ) {
        return getShowWithReviewStatsListResult(showService.search(pageable, keyword));
    }

    @GetMapping("/facilities/{facilityId}/shows")
    public ListResult<With<ShowResponse, ShowReviewStatsResponse>> getShowListOfFacility(
            Pageable pageable, @PathVariable FacilityId facilityId, @RequestParam(required = false) ShowGenre genre
    ) {
        return getShowWithReviewStatsListResult(showService.getListOfFacility(pageable, facilityId, genre));
    }

    private ListResult<With<ShowResponse, ShowReviewStatsResponse>> getShowWithReviewStatsListResult(
            final List<ShowResponse> showResponses
    ) {
        final List<ShowId> showIds = showResponses.stream().map(ShowResponse::id).toList();
        final Map<ShowId, ShowReviewStatsResponse> showIdToStats = showReviewStatsQueryService.getList(showIds)
                .stream()
                .collect(Collectors.toMap(ShowReviewStatsDto::showId, ShowReviewStatsResponse::of));
        return new ListResult<>(showResponses.stream()
                .map(show -> new With<>(
                        show, showIdToStats.getOrDefault(show.id(), new ShowReviewStatsResponse(0, 0L, 0D)
                ))).toList()
        );
    }
}
