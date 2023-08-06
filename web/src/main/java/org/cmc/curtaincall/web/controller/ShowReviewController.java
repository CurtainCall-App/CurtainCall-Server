package org.cmc.curtaincall.web.controller;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.review.ShowReviewService;
import org.cmc.curtaincall.web.service.review.request.ShowReviewCreate;
import org.cmc.curtaincall.web.service.review.response.ShowReviewResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ShowReviewController {

    private final ShowReviewService showReviewService;

    @PostMapping("/shows/{showId}/reviews")
    public IdResult<Long> createShowReview(
            @PathVariable String showId, @Validated @RequestBody ShowReviewCreate showReviewCreate) {
        return showReviewService.create(showId, showReviewCreate);
    }

    @GetMapping("/shows/{showId}/reviews")
    public Slice<ShowReviewResponse> getList(Pageable pageable, @PathVariable String showId) {
        return showReviewService.getList(pageable, showId);
    }
}
