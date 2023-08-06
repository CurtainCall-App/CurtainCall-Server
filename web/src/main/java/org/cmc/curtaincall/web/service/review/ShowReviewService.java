package org.cmc.curtaincall.web.service.review;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.review.ShowReview;
import org.cmc.curtaincall.domain.review.repository.ShowReviewRepository;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.repository.ShowRepository;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.review.request.ShowReviewCreate;
import org.cmc.curtaincall.web.service.review.response.ShowReviewResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    public Slice<ShowReviewResponse> getList(Pageable pageable, String showId) {
        Show show = showRepository.getReferenceById(showId);
        return showReviewRepository.findSliceByShowAndUseYnIsTrue(pageable, show)
                .map(showReview -> ShowReviewResponse.builder()
                        .id(showReview.getId())
                        .showId(showReview.getShow().getId())
                        .grade(showReview.getGrade())
                        .content(showReview.getContent())
                        .creatorId(showReview.getCreatedBy().getId())
                        .creatorNickname(showReview.getCreatedBy().getNickname())
                        .creatorImageUrl(showReview.getCreatedBy().getImage().getUrl())
                        .build()
                );
    }

    private Show getShowById(String id) {
        return showRepository.findById(id)
                .filter(Show::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Show id=" + id));
    }
}
