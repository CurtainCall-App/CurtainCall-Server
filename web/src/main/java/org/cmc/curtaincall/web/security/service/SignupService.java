package org.cmc.curtaincall.web.security.service;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.account.Account;
import org.cmc.curtaincall.domain.account.repository.AccountRepository;
import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.member.exception.MemberNicknameAlreadyExistsException;
import org.cmc.curtaincall.domain.member.repository.MemberRepository;
import org.cmc.curtaincall.web.security.request.SignupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final AccountRepository accountRepository;

    private final MemberRepository memberRepository;

    public boolean checkNicknameDuplicate(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Transactional
    public MemberId signup(final String username, final SignupRequest request) {
        validateNickname(request.getNickname());
        final Member member = memberRepository.save(Member.builder()
                .nickname(request.getNickname())
                .build());
        final Account account = accountRepository.findByUsername(username)
                .orElseGet(() -> accountRepository.save(new Account(username)));
        final MemberId memberId = new MemberId(member.getId());
        account.signup(memberId);
        return memberId;
    }

    private void validateNickname(final String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new MemberNicknameAlreadyExistsException(nickname);
        }
    }
}
