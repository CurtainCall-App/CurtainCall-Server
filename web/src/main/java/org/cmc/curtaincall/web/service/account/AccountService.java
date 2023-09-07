package org.cmc.curtaincall.web.service.account;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.account.Account;
import org.cmc.curtaincall.domain.account.repository.AccountRepository;
import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.member.repository.MemberRepository;
import org.cmc.curtaincall.web.exception.AlreadySignupAccountException;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.cmc.curtaincall.web.service.account.response.AccountDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;

    private final MemberRepository memberRepository;

    public Long getMemberId(String username) {
        Account account = getAccountByUsername(username);
        return Optional.ofNullable(account.getMember())
                .map(Member::getId)
                .orElse(null);
    }

    @Transactional
    public AccountDto login(String username, String refreshToken, LocalDateTime refreshTokenExpiresAt) {
        Account account = getOrCreate(username);
        account.renewRefreshToken(refreshToken, refreshTokenExpiresAt);
        return AccountDto.of(account);
    }

    @Transactional
    public void logout(String username) {
        Account account = getAccountByUsername(username);
        account.refreshTokenExpires();
    }

    private Account getOrCreate(String username) {
        Optional<Account> accountOptional = accountRepository.findByUsernameAndUseYnIsTrue(username);
        if (accountOptional.isEmpty()) {
            Account account = accountRepository.save(Account.builder()
                    .username(username)
                    .build());
            accountOptional = Optional.of(account);
        }
        Account account = accountOptional.get();
        if (Boolean.FALSE.equals(account.getUseYn())) {
            throw new EntityNotFoundException("deleted Account.username=" + username);
        }
        return account;
    }

    public AccountDto get(String username) {
        Account account = getAccountByUsername(username);
        return AccountDto.of(account);
    }

    @Transactional
    public void signupMember(String username, Long memberId) {
        Account account = getAccountByUsername(username);
        if (account.getMember() != null) {
            throw new AlreadySignupAccountException("username=" + username);
        }
        Member member = getMemberById(memberId);
        account.setMember(member);
    }

    @Transactional
    public void delete(String username) {
        Account account = getAccountByUsername(username);
        accountRepository.delete(account);
    }

    @Transactional
    public void withdraw(Long memberId) {
        accountRepository.findByMember(memberRepository.getReferenceById(memberId))
                .ifPresent(account -> account.setMember(null));
    }

    private Account getAccountByUsername(String username) {
        return accountRepository.findByUsernameAndUseYnIsTrue(username)
                .orElseThrow(() -> new EntityNotFoundException("Account.username=" + username));
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .filter(Member::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Member id=" + memberId));
    }
}
