package org.cmc.curtaincall.web.member;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.account.repository.AccountRepository;
import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.member.MemberHelper;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.member.MemberWithdrawal;
import org.cmc.curtaincall.domain.member.MemberWithdrawalQueryDao;
import org.cmc.curtaincall.domain.member.repository.MemberRepository;
import org.cmc.curtaincall.domain.member.repository.MemberWithdrawalRepository;
import org.cmc.curtaincall.domain.review.ShowReviewId;
import org.cmc.curtaincall.web.lostitem.LostItemDeleteService;
import org.cmc.curtaincall.web.member.request.MemberWithdraw;
import org.cmc.curtaincall.web.party.PartyDeleteService;
import org.cmc.curtaincall.web.party.PartyParticipationService;
import org.cmc.curtaincall.web.review.ShowReviewDeleteService;
import org.cmc.curtaincall.web.review.ShowReviewLikeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberWithdrawService {

    private final MemberWithdrawalRepository withdrawalRepository;

    private final MemberWithdrawalQueryDao memberWithdrawalQueryDao;

    private final MemberRepository memberRepository;

    private final ShowReviewLikeService showReviewLikeService;

    private final ShowReviewDeleteService showReviewDeleteService;

    private final PartyParticipationService partyParticipationService;

    private final PartyDeleteService partyDeleteService;

    private final LostItemDeleteService lostItemDeleteService;

    private final AccountRepository accountRepository;

    @Transactional
    public void withdraw(final MemberId id, final MemberWithdraw memberWithdraw) {
        final Member member = MemberHelper.get(id, memberRepository);
        final MemberId memberId = new MemberId(member.getId());
        deleteAllShowReviewLike(memberId);
        deleteAllShowReview(memberId);
        deleteAllParticipation(memberId);
        deleteAllParty(memberId);
        deleteAllLostItem(memberId);

        accountRepository.findByMemberId(memberId).ifPresent(accountRepository::delete);
        memberRepository.delete(member);
        withdrawalRepository.save(MemberWithdrawal.builder()
                .memberId(id)
                .reason(memberWithdraw.getReason())
                .content(memberWithdraw.getContent())
                .build());
    }

    private void deleteAllShowReviewLike(final MemberId memberId) {
        final List<ShowReviewId> likedShowReviewIds = memberWithdrawalQueryDao
                .findAllLikedShowReviewIdByMemberId(memberId);
        likedShowReviewIds.forEach(id -> showReviewLikeService.cancelLike(memberId, id));
    }

    private void deleteAllShowReview(final MemberId memberId) {
        final List<ShowReviewId> showReviewIds = memberWithdrawalQueryDao.findAllShowReviewIdByMemberId(memberId);
        showReviewIds.forEach(showReviewDeleteService::delete);
    }

    private void deleteAllParticipation(final MemberId memberId) {
        memberWithdrawalQueryDao.findAllParticipatedPartyIdByMemberId(memberId)
                .forEach(partyId -> partyParticipationService.leave(partyId, memberId));
    }

    private void deleteAllParty(final MemberId memberId) {
        memberWithdrawalQueryDao.findAllRecruitingPartyIdByMemberId(memberId)
                .forEach(partyDeleteService::delete);
    }

    private void deleteAllLostItem(final MemberId memberId) {
        memberWithdrawalQueryDao.findAllLostItemIdByMemberId(memberId)
                .forEach(lostItemDeleteService::delete);
    }
}
