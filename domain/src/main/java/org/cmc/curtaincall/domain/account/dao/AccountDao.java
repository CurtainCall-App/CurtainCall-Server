package org.cmc.curtaincall.domain.account.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.member.MemberId;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.cmc.curtaincall.domain.account.QAccount.account;

@RequiredArgsConstructor
@Repository
public class AccountDao {

    private final JPAQueryFactory query;

    public Optional<MemberId> findMemberIdByUsername(String username) {
        return Optional.ofNullable(query.select(account.memberId)
                .from(account)
                .where(account.username.eq(username))
                .fetchOne());
    }
}
