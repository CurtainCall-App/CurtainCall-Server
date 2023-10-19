package org.cmc.curtaincall.domain.account;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseTimeEntity;
import org.cmc.curtaincall.domain.member.MemberId;
import org.springframework.data.domain.Persistable;

import java.util.Objects;

@Entity
@Table(name = "account",
        indexes = {
                @Index(name = "IX_account__member", columnList = "member_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseTimeEntity implements Persistable<String> {

    @Id
    @Column(name = "username", nullable = false)
    private String username;

    @Embedded
    private MemberId memberId;

    public Account(final String username) {
        this.username = username;
        this.memberId = null;
    }

    public void registerMember(final MemberId memberId) {
        this.memberId = Objects.requireNonNull(memberId);
    }

    @Override
    public String getId() {
        return this.username;
    }

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }
}
