package org.cmc.curtaincall.web.controller;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.web.service.kopis.KopisService;
import org.cmc.curtaincall.web.service.kopis.request.ShowListRequest;
import org.cmc.curtaincall.web.service.kopis.response.ShowDetailResponse;
import org.cmc.curtaincall.web.service.kopis.response.ShowResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ShowController {

    private final KopisService kopisService;

    @GetMapping("/shows")
    public Slice<ShowResponse> getShows(@Validated @ModelAttribute ShowListRequest showListRequest, Pageable pageable) {
        return kopisService.getShows(showListRequest, pageable);
    }

    @GetMapping("/shows/{showId}")
    public ShowDetailResponse getShowDetail(@PathVariable String showId) {
        return kopisService.getShowDetail(showId);
    }
}
