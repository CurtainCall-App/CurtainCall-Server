package org.cmc.curtaincall.web.security;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.account.Account;
import org.cmc.curtaincall.domain.account.exception.AccountNotFoundException;
import org.cmc.curtaincall.domain.account.repository.AccountRepository;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.web.exception.AlreadySignupAccountException;
import org.cmc.curtaincall.web.security.response.AccountDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountDto get(String username) {
        Account account = getAccountByUsername(username);
        return AccountDto.of(account);
    }

    @Transactional
    public void signupMember(String username, Long memberId) {
        Account account = getAccountByUsername(username);
        if (account.getMemberId() != null) {
            throw new AlreadySignupAccountException("username=" + username);
        }
        account.registerMember(new MemberId(memberId));
    }

    @Transactional
    public void delete(MemberId memberId) {
        Account account = getAccountByMember(memberId);
        accountRepository.delete(account);
    }

    private Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username)
                .filter(Account::getUseYn)
                .orElseThrow(() -> new AccountNotFoundException("Account.username=" + username));
    }

    private Account getAccountByMember(MemberId memberId) {
        return accountRepository.findByMemberId(memberId)
                .filter(Account::getUseYn)
                .orElseThrow(() -> new AccountNotFoundException("Account.memberId=" + memberId));
    }
}
