package org.cmc.curtaincall.domain.review.repository;

import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.review.ShowReview;
import org.cmc.curtaincall.domain.review.ShowReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ShowReviewLikeRepository extends JpaRepository<ShowReviewLike, Long> {

    boolean existsByMemberIdAndShowReview(MemberId memberId, ShowReview showReview);

    Optional<ShowReviewLike> findByMemberIdAndShowReview(MemberId memberId, ShowReview showReview);

    List<ShowReviewLike> findAllByMemberIdAndShowReviewIn(MemberId memberId, Collection<ShowReview> reviews);
}
