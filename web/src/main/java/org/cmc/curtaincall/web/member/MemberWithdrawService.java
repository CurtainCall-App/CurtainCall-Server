package org.cmc.curtaincall.web.member;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.member.MemberHelper;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.member.MemberWithdrawal;
import org.cmc.curtaincall.domain.member.event.MemberWithdrewEvent;
import org.cmc.curtaincall.domain.member.repository.MemberRepository;
import org.cmc.curtaincall.domain.member.repository.MemberWithdrawalRepository;
import org.cmc.curtaincall.web.member.request.MemberWithdraw;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberWithdrawService {

    private final MemberWithdrawalRepository withdrawalRepository;

    private final MemberRepository memberRepository;

    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void withdraw(MemberId id, MemberWithdraw memberWithdraw) {
        final Member member = MemberHelper.get(id, memberRepository);
        member.delete();
        withdrawalRepository.save(MemberWithdrawal.builder()
                .memberId(id)
                .reason(memberWithdraw.getReason())
                .content(memberWithdraw.getContent())
                .build());
        eventPublisher.publishEvent(new MemberWithdrewEvent(id));
    }
}
