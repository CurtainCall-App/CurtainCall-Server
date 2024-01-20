package org.cmc.curtaincall.web.recommend;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.web.common.response.ListResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ShowRecommendationController {

    private final ShowRecommendationService showRecommendationService;

    @GetMapping("/show-recommendations")
    public ListResult<ShowRecommendationResponse> getList() {
        return new ListResult<>(showRecommendationService.getList());
    }
}
