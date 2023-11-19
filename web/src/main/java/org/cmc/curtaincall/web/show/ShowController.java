package org.cmc.curtaincall.web.show;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.web.common.response.With;
import org.cmc.curtaincall.web.show.request.ShowListRequest;
import org.cmc.curtaincall.web.show.response.ShowDateTimeResponse;
import org.cmc.curtaincall.web.show.response.ShowDetailResponse;
import org.cmc.curtaincall.web.show.response.ShowResponse;
import org.cmc.curtaincall.web.show.response.ShowReviewStatsDto;
import org.cmc.curtaincall.web.show.response.ShowReviewStatsResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.web.SortDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class ShowController {

    private final ShowService showService;

    private final ShowReviewStatsQueryService showReviewStatsQueryService;

    @GetMapping("/shows")
    public Slice<ShowResponse> getShows(@Validated @ModelAttribute ShowListRequest showListRequest, Pageable pageable) {
        return showService.getList(showListRequest, pageable);
    }

    @GetMapping("/shows/{showId}")
    public With<ShowDetailResponse, ShowReviewStatsResponse> getShowDetail(@PathVariable ShowId showId) {
        final ShowDetailResponse showDetailResponse = showService.getDetail(showId);
        final ShowReviewStatsDto showReviewStatsDto = showReviewStatsQueryService.get(showId);
        return new With<>(showDetailResponse, ShowReviewStatsResponse.of(showReviewStatsDto));
    }

    @GetMapping("/shows-to-open")
    public Slice<ShowResponse> getShowListToOpen(
            @SortDefault(sort = "startDate") Pageable pageable,
            @RequestParam LocalDate startDate) {
        return showService.getListToOpen(pageable, startDate);
    }

    @GetMapping("/shows-to-end")
    public Slice<ShowResponse> getShowListToEnd(
            @SortDefault(sort = "endDate") Pageable pageable,
            @RequestParam LocalDate endDate,
            @RequestParam(required = false) ShowGenre genre
    ) {
        return showService.getListToEnd(pageable, endDate, genre);
    }

    @GetMapping("/search/shows")
    public Slice<ShowResponse> searchShows(
            Pageable pageable, @RequestParam @Validated @Size(max = 100) @NotBlank String keyword) {
        return showService.search(pageable, keyword);
    }

    @GetMapping("/livetalk-show-times")
    public Slice<ShowDateTimeResponse> getLiveTalkShowTimeList(@RequestParam LocalDateTime baseDateTime) {
        return new SliceImpl<>(showService.getLiveTalkShowTimeList(baseDateTime));
    }

}
