package org.cmc.curtaincall.web.service.review;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.member.repository.MemberRepository;
import org.cmc.curtaincall.domain.review.ShowReview;
import org.cmc.curtaincall.domain.review.ShowReviewLike;
import org.cmc.curtaincall.domain.review.repository.ShowReviewLikeRepository;
import org.cmc.curtaincall.domain.review.repository.ShowReviewRepository;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShowReviewLikeService {

    private final ShowReviewLikeRepository showReviewLikeRepository;

    private final ShowReviewRepository showReviewRepository;

    private final MemberRepository memberRepository;

    @Transactional
    public void like(Long memberId, Long reviewId) {
        ShowReview showReview = getShowReviewById(reviewId);
        Member member = memberRepository.getReferenceById(memberId);
        if (showReviewLikeRepository.existsByMemberAndShowReview(member, showReview)) {
            return;
        }
        showReviewLikeRepository.save(new ShowReviewLike(showReview, member));
        showReview.plusLikeCount();
    }

    private ShowReview getShowReviewById(Long id) {
        return showReviewRepository.findById(id)
                .filter(ShowReview::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("ShowReview id=" + id));
    }
}
