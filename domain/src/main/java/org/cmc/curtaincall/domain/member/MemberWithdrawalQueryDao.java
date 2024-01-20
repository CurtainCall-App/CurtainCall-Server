package org.cmc.curtaincall.domain.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.cmc.curtaincall.domain.lostitem.LostItemId;
import org.cmc.curtaincall.domain.party.PartyId;
import org.cmc.curtaincall.domain.party.PartyMemberRole;
import org.cmc.curtaincall.domain.review.ShowReviewId;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.cmc.curtaincall.domain.lostitem.QLostItem.lostItem;
import static org.cmc.curtaincall.domain.party.QPartyMember.partyMember;
import static org.cmc.curtaincall.domain.review.QShowReview.showReview;
import static org.cmc.curtaincall.domain.review.QShowReviewLike.showReviewLike;

@Repository
public class MemberWithdrawalQueryDao {

    private final JPAQueryFactory query;

    public MemberWithdrawalQueryDao(final EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public List<ShowReviewId> findAllLikedShowReviewIdByMemberId(final MemberId memberId) {
        return query
                .select(showReviewLike.showReview.id)
                .from(showReviewLike)
                .where(showReviewLike.memberId.eq(memberId))
                .fetch()
                .stream()
                .map(ShowReviewId::new)
                .toList();
    }

    public List<ShowReviewId> findAllShowReviewIdByMemberId(final MemberId memberId) {
        return query
                .select(showReview.id)
                .from(showReview)
                .where(showReview.createdBy.memberId.eq(memberId))
                .fetch()
                .stream()
                .map(ShowReviewId::new)
                .toList();
    }

    public List<PartyId> findAllParticipatedPartyIdByMemberId(final MemberId memberId) {
        return query
                .select(partyMember.party.id)
                .from(partyMember)
                .where(
                        partyMember.memberId.eq(memberId),
                        partyMember.role.eq(PartyMemberRole.PARTICIPANT)
                )
                .fetch()
                .stream()
                .map(PartyId::new)
                .toList();
    }

    public List<PartyId> findAllRecruitingPartyIdByMemberId(final MemberId memberId) {
        return query
                .select(partyMember.party.id)
                .from(partyMember)
                .where(
                        partyMember.memberId.eq(memberId),
                        partyMember.role.eq(PartyMemberRole.RECRUITER)
                )
                .fetch()
                .stream()
                .map(PartyId::new)
                .toList();
    }

    public List<LostItemId> findAllLostItemIdByMemberId(final MemberId memberId) {
        return query
                .select(lostItem.id)
                .from(lostItem)
                .where(lostItem.createdBy.memberId.eq(memberId))
                .fetch()
                .stream()
                .map(LostItemId::new)
                .toList();
    }
}
