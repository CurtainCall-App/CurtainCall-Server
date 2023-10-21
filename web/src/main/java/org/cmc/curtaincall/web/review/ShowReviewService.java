package org.cmc.curtaincall.web.review;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.OptimisticLock;
import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.member.repository.MemberRepository;
import org.cmc.curtaincall.domain.review.ShowReview;
import org.cmc.curtaincall.domain.review.ShowReviewEditor;
import org.cmc.curtaincall.domain.review.exception.ShowReviewNotFoundException;
import org.cmc.curtaincall.domain.review.repository.ShowReviewRepository;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.domain.show.repository.ShowRepository;
import org.cmc.curtaincall.web.common.response.IdResult;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.cmc.curtaincall.web.review.request.ShowReviewCreate;
import org.cmc.curtaincall.web.review.request.ShowReviewEdit;
import org.cmc.curtaincall.web.review.response.ShowReviewMyResponse;
import org.cmc.curtaincall.web.review.response.ShowReviewResponse;
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

    private final MemberRepository memberRepository;

    @Transactional
    @OptimisticLock
    public IdResult<Long> create(String showId, ShowReviewCreate showReviewCreate) {
        Show show = getShowWithLockById(showId);
        ShowReview showReview = showReviewRepository.save(ShowReview.builder()
                .showId(new ShowId(showId))
                .grade(showReviewCreate.getGrade())
                .content(showReviewCreate.getContent())
                .build());
        show.applyReview(showReview);
        return new IdResult<>(showReview.getId());
    }

    public Slice<ShowReviewResponse> getList(Pageable pageable, String showId) {
        Show show = showRepository.getReferenceById(showId);
        return showReviewRepository.findSliceByShowIdAndUseYnIsTrue(pageable, new ShowId(showId))
                .map(ShowReviewResponse::of);
    }

    public Slice<ShowReviewMyResponse> getMyList(Pageable pageable, Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);
        return showReviewRepository.findSliceByCreatedByAndUseYnIsTrue(pageable, member)
                .map(showReview -> ShowReviewMyResponse.builder()
                        .id(showReview.getId())
                        .showId(showReview.getShowId().getId())
                        .showName(getShowById(showReview.getShowId().getId()).getName())
                        .grade(showReview.getGrade())
                        .content(showReview.getContent())
                        .createdAt(showReview.getCreatedAt())
                        .likeCount(showReview.getLikeCount())
                        .build()
                );
    }

    @Transactional
    @OptimisticLock
    public void delete(Long id) {
        ShowReview showReview = getShowReviewById(id);
        Show show = getShowWithLockById(showReview.getShowId().getId());
        show.cancelReview(showReview);
        showReviewRepository.delete(showReview);
    }

    @Transactional
    @OptimisticLock
    public void edit(ShowReviewEdit showReviewEdit, Long id) {
        ShowReview showReview = getShowReviewWithLockById(id);
        int prevReviewGrade = showReview.getGrade();

        ShowReviewEditor editor = showReview.toEditor()
                .grade(showReviewEdit.getGrade())
                .content(showReviewEdit.getContent())
                .build();

        Show show = getShowWithLockById(showReview.getShowId().getId());
        show.applyReviewEdit(showReview, prevReviewGrade);

        showReview.edit(editor);
    }

    public boolean isOwnedByMember(Long reviewId, Long memberId) {
        ShowReview showReview = getShowReviewById(reviewId);
        return Objects.equals(showReview.getCreatedBy().getId(), memberId);
    }

    private ShowReview getShowReviewById(Long id) {
        return showReviewRepository.findById(id)
                .filter(ShowReview::getUseYn)
                .orElseThrow(() -> new ShowReviewNotFoundException(id));
    }

    private ShowReview getShowReviewWithLockById(Long id) {
        return showReviewRepository.findWithLockById(id)
                .filter(ShowReview::getUseYn)
                .orElseThrow(() -> new ShowReviewNotFoundException(id));
    }

    private Show getShowWithLockById(String id) {
        return showRepository.findWithLockById(id)
                .filter(Show::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Show id=" + id));
    }

    private Show getShowById(String id) {
        return showRepository.findById(id)
                .filter(Show::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Show id=" + id));
    }

}
