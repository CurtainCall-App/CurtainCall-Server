package org.cmc.curtaincall.web.review;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.OptimisticLock;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.review.ShowReview;
import org.cmc.curtaincall.domain.review.ShowReviewHelper;
import org.cmc.curtaincall.domain.review.ShowReviewId;
import org.cmc.curtaincall.domain.review.ShowReviewLike;
import org.cmc.curtaincall.domain.review.repository.ShowReviewLikeRepository;
import org.cmc.curtaincall.domain.review.repository.ShowReviewRepository;
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
    public void like(final MemberId memberId, final ShowReviewId reviewId) {
        ShowReview showReview = ShowReviewHelper.getWithOptimisticLock(reviewId, showReviewRepository);
        if (showReviewLikeRepository.existsByMemberIdAndShowReview(memberId, showReview)) {
            return;
        }
        showReviewLikeRepository.save(new ShowReviewLike(showReview, memberId));
        showReview.plusLikeCount();
    }

    @OptimisticLock
    @Transactional
    public void cancelLike(final MemberId memberId, final ShowReviewId reviewId) {
        ShowReview showReview = ShowReviewHelper.get(reviewId, showReviewRepository);
        showReviewLikeRepository.findByMemberIdAndShowReview(memberId, showReview)
                .ifPresent(showReviewLikeRepository::delete);
        showReview.minusLikeCount();
    }

    public List<ShowReviewLikedResponse> areLiked(final MemberId memberId, final List<ShowReviewId> reviewIds) {
        List<ShowReview> reviews = reviewIds.stream()
                .map(ShowReviewId::getId)
                .map(showReviewRepository::getReferenceById)
                .toList();
        Set<ShowReviewId> likedReviewIds = showReviewLikeRepository.findAllByMemberIdAndShowReviewIn(memberId, reviews).stream()
                .map(showReviewLike -> new ShowReviewId(showReviewLike.getShowReview().getId()))
                .collect(Collectors.toSet());
        return reviewIds.stream()
                .map(reviewId -> new ShowReviewLikedResponse(reviewId.getId(), likedReviewIds.contains(reviewId)))
                .toList();
    }

}
