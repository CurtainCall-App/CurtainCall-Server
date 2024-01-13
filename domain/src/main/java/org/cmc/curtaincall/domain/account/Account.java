package org.cmc.curtaincall.domain.account;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseTimeEntity;
import org.cmc.curtaincall.domain.member.MemberId;

@Entity
@Table(name = "account",
        indexes = {
                @Index(name = "IX_account__member", columnList = "member_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_account__username", columnNames = "username")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Embedded
    @AttributeOverride(
            name = "id",
            column = @Column(name = "member_id")
    )
    private MemberId memberId;

    @Builder
    public Account(final String username, final MemberId memberId) {
        this.username = username;
        this.memberId = memberId;
    }

}
