package org.cmc.curtaincall.domain.review.repository;

import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.review.ShowReview;
import org.cmc.curtaincall.domain.review.ShowReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowReviewLikeRepository extends JpaRepository<ShowReviewLike, Long> {

    boolean existsByMemberAndShowReview(Member member, ShowReview showReview);
}
