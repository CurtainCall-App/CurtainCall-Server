package org.cmc.curtaincall.web.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.web.service.show.ShowService;
import org.cmc.curtaincall.web.service.show.request.ShowListRequest;
import org.cmc.curtaincall.web.service.show.response.ShowDetailResponse;
import org.cmc.curtaincall.web.service.show.response.ShowResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/search/shows")
    public Slice<ShowResponse> searchShows(
            Pageable pageable, @RequestParam @Validated @Size(max = 100) @NotBlank String keyword) {
        return showService.search(pageable, keyword);
    }

}
