package org.cmc.curtaincall.domain.review.repository;

import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.review.ShowReview;
import org.cmc.curtaincall.domain.review.ShowReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ShowReviewLikeRepository extends JpaRepository<ShowReviewLike, Long> {

    boolean existsByMemberAndShowReview(Member member, ShowReview showReview);

    Optional<ShowReviewLike> findByMemberAndShowReview(Member member, ShowReview showReview);

    List<ShowReviewLike> findAllByMemberAndShowReviewIn(Member member, Collection<ShowReview> reviews);
}
