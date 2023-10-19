package org.cmc.curtaincall.domain.account;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseTimeEntity;
import org.cmc.curtaincall.domain.member.MemberId;

import java.util.Objects;

@Entity
@Table(name = "account",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_account__username", columnNames = "username")
        },
        indexes = {
                @Index(name = "IX_account__member", columnList = "member_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Embedded
    private MemberId memberId;

    @Column(name = "username", nullable = false)
    private String username;

    public Account(final String username) {
        this.username = username;
        this.memberId = null;
    }

    public void registerMember(final MemberId memberId) {
        this.memberId = Objects.requireNonNull(memberId);
    }
}
