package org.cmc.curtaincall.domain.account.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.account.exception.AccountNotFoundException;
import org.cmc.curtaincall.domain.member.MemberId;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.cmc.curtaincall.domain.account.QAccount.account;
import static org.cmc.curtaincall.domain.member.QMember.member;

@RequiredArgsConstructor
@Repository
public class AccountDao {

    private final JPAQueryFactory query;

    public Optional<MemberId> findMemberIdByUsername(final String username) {
        return Optional.ofNullable(query.select(account.memberId)
                .from(account)
                .join(member).on(member.id.eq(account.memberId.id))
                .where(
                        account.username.eq(username),
                        member.useYn

                )
                .fetchOne());
    }

    public MemberId getMemberId(final String username) {
        return findMemberIdByUsername(username)
                .orElseThrow(() -> new AccountNotFoundException(username));
    }
}
