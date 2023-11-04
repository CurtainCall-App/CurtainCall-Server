package org.cmc.curtaincall.web.review;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.OptimisticLock;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.review.ShowReview;
import org.cmc.curtaincall.domain.review.ShowReviewLike;
import org.cmc.curtaincall.domain.review.repository.ShowReviewLikeRepository;
import org.cmc.curtaincall.domain.review.repository.ShowReviewRepository;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.cmc.curtaincall.web.review.response.ShowReviewLikedResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShowReviewLikeService {

    private final ShowReviewLikeRepository showReviewLikeRepository;

    private final ShowReviewRepository showReviewRepository;

    @OptimisticLock
    @Transactional
    public void like(Long memberId, Long reviewId) {
        ShowReview showReview = getShowReviewWithLockById(reviewId);
        if (showReviewLikeRepository.existsByMemberIdAndShowReview(new MemberId(memberId), showReview)) {
            return;
        }
        showReviewLikeRepository.save(new ShowReviewLike(showReview, new MemberId(memberId)));
        showReview.plusLikeCount();
    }

    @OptimisticLock
    @Transactional
    public void cancelLike(Long memberId, Long reviewId) {
        ShowReview showReview = getShowReviewById(reviewId);
        showReviewLikeRepository.findByMemberIdAndShowReview(new MemberId(memberId), showReview)
                .ifPresent(showReviewLikeRepository::delete);
        showReview.minusLikeCount();
    }

    public List<ShowReviewLikedResponse> areLiked(Long memberId, List<Long> reviewIds) {
        List<ShowReview> reviews = reviewIds.stream()
                .map(showReviewRepository::getReferenceById)
                .toList();
        Set<Long> likedReviewIds = showReviewLikeRepository.findAllByMemberIdAndShowReviewIn(new MemberId(memberId), reviews).stream()
                .map(showReviewLike -> showReviewLike.getShowReview().getId())
                .collect(Collectors.toSet());
        return reviewIds.stream()
                .map(reviewId -> new ShowReviewLikedResponse(reviewId, likedReviewIds.contains(reviewId)))
                .toList();
    }

    private ShowReview getShowReviewById(Long id) {
        return showReviewRepository.findById(id)
                .filter(ShowReview::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("ShowReview id=" + id));
    }

    private ShowReview getShowReviewWithLockById(Long id) {
        return showReviewRepository.findWithLockById(id)
                .filter(ShowReview::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("ShowReview id=" + id));
    }
}
