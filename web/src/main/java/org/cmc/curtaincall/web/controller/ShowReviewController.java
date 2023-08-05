package org.cmc.curtaincall.web.controller;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.review.ShowReviewService;
import org.cmc.curtaincall.web.service.review.request.ShowReviewCreate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ShowReviewController {

    private final ShowReviewService showReviewService;

    @PostMapping("/shows/{showId}/reviews")
    public IdResult<Long> createShowReview(
            @PathVariable String showId, @Validated @RequestBody ShowReviewCreate showReviewCreate) {
        return showReviewService.create(showId, showReviewCreate);
    }
}
