package org.cmc.curtaincall.web.service.review;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.review.ShowReview;
import org.cmc.curtaincall.domain.review.ShowReviewEditor;
import org.cmc.curtaincall.domain.review.repository.ShowReviewRepository;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.repository.ShowRepository;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.review.request.ShowReviewCreate;
import org.cmc.curtaincall.web.service.review.request.ShowReviewEdit;
import org.cmc.curtaincall.web.service.review.response.ShowReviewResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShowReviewService {

    private final ShowRepository showRepository;

    private final ShowReviewRepository showReviewRepository;

    @Transactional
    public IdResult<Long> create(String showId, ShowReviewCreate showReviewCreate) {
        Show show = getShowById(showId);
        ShowReview showReview = showReviewRepository.save(ShowReview.builder()
                .show(show)
                .grade(showReviewCreate.getGrade())
                .content(showReviewCreate.getContent())
                .build());
        show.applyReview(showReview);
        return new IdResult<>(showReview.getId());
    }

    public Slice<ShowReviewResponse> getList(Pageable pageable, String showId) {
        Show show = showRepository.getReferenceById(showId);
        return showReviewRepository.findSliceByShowAndUseYnIsTrue(pageable, show)
                .map(ShowReviewResponse::of);
    }

    @Transactional
    public void delete(Long id) {
        ShowReview showReview = getShowReviewById(id);
        Show show = showReview.getShow();
        show.cancelReview(showReview);
        showReview.delete();
    }

    @Transactional
    public void edit(ShowReviewEdit showReviewEdit, Long id) {
        ShowReview showReview = getShowReviewById(id);
        int prevReviewGrade = showReviewEdit.getGrade();

        ShowReviewEditor editor = showReview.toEditor()
                .grade(showReviewEdit.getGrade())
                .content(showReviewEdit.getContent())
                .build();

        Show show = showReview.getShow();
        show.applyReviewEdit(showReview, prevReviewGrade);

        showReview.edit(editor);
    }

    public boolean isOwnedByMember(Long reviewId, Long memberId) {
        ShowReview showReview = getShowReviewById(reviewId);
        return Objects.equals(showReview.getCreatedBy().getId(), memberId);
    }

    private Show getShowById(String id) {
        return showRepository.findById(id)
                .filter(Show::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Show id=" + id));
    }

    private ShowReview getShowReviewById(Long id) {
        return showReviewRepository.findById(id)
                .filter(ShowReview::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("ShowReview id=" + id));
    }

}
