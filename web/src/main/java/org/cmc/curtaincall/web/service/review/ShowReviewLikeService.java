package org.cmc.curtaincall.web.service.review;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.OptimisticLock;
import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.member.repository.MemberRepository;
import org.cmc.curtaincall.domain.review.ShowReview;
import org.cmc.curtaincall.domain.review.ShowReviewLike;
import org.cmc.curtaincall.domain.review.repository.ShowReviewLikeRepository;
import org.cmc.curtaincall.domain.review.repository.ShowReviewRepository;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.cmc.curtaincall.web.service.review.response.ShowReviewLikedResponse;
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

    private final MemberRepository memberRepository;

    @OptimisticLock
    @Transactional
    public void like(Long memberId, Long reviewId) {
        ShowReview showReview = getShowReviewWithLockById(reviewId);
        Member member = memberRepository.getReferenceById(memberId);
        if (showReviewLikeRepository.existsByMemberAndShowReview(member, showReview)) {
            return;
        }
        showReviewLikeRepository.save(new ShowReviewLike(showReview, member));
        showReview.plusLikeCount();
    }

    @OptimisticLock
    @Transactional
    public void cancelLike(Long memberId, Long reviewId) {
        ShowReview showReview = getShowReviewById(reviewId);
        Member member = memberRepository.getReferenceById(memberId);
        showReviewLikeRepository.findByMemberAndShowReview(member, showReview)
                .ifPresent(showReviewLikeRepository::delete);
        showReview.minusLikeCount();
    }

    public List<ShowReviewLikedResponse> areLiked(Long memberId, List<Long> reviewIds) {
        Member member = memberRepository.getReferenceById(memberId);
        List<ShowReview> reviews = reviewIds.stream()
                .map(showReviewRepository::getReferenceById)
                .toList();
        Set<Long> likedReviewIds = showReviewLikeRepository.findAllByMemberAndShowReviewIn(member, reviews).stream()
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
