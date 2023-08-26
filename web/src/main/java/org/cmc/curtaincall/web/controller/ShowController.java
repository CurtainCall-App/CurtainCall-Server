package org.cmc.curtaincall.web.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.web.service.show.ShowService;
import org.cmc.curtaincall.web.service.show.request.ShowListRequest;
import org.cmc.curtaincall.web.service.show.response.ShowDetailResponse;
import org.cmc.curtaincall.web.service.show.response.ShowResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class ShowController {

    private final ShowService showService;

    @GetMapping("/shows")
    public Slice<ShowResponse> getShows(@Validated @ModelAttribute ShowListRequest showListRequest, Pageable pageable) {
        return showService.getList(showListRequest, pageable);
    }

    @GetMapping("/shows/{showId}")
    public ShowDetailResponse getShowDetail(@PathVariable String showId) {
        return showService.getDetail(showId);
    }

    @GetMapping("/shows-to-open")
    public Slice<ShowResponse> getShowListToOpen(
            Pageable pageable,
            @RequestParam @Validated @DateTimeFormat(pattern = "yyyy-MM-dd") @NotNull LocalDate startDate) {
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

}
