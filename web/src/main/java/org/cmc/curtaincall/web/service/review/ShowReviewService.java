package org.cmc.curtaincall.web.service.review;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.review.ShowReview;
import org.cmc.curtaincall.domain.review.repository.ShowReviewRepository;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.repository.ShowRepository;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.review.request.ShowReviewCreate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShowReviewService {

    private final ShowRepository showRepository;

    private final ShowReviewRepository showReviewRepository;

    public IdResult<Long> create(String showId, ShowReviewCreate showReviewCreate) {
        Show show = getShowById(showId);
        ShowReview showReview = showReviewRepository.save(ShowReview.builder()
                .show(show)
                .grade(showReviewCreate.getGrade())
                .content(showReviewCreate.getContent())
                .build());
        return new IdResult<>(showReview.getId());
    }

    private Show getShowById(String id) {
        return showRepository.findById(id)
                .filter(Show::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Show id=" + id));
    }
}
