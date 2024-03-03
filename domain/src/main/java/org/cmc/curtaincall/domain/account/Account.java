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
import org.cmc.curtaincall.domain.account.exception.AccountAlreadySignupException;
import org.cmc.curtaincall.domain.core.BaseTimeEntity;
import org.cmc.curtaincall.domain.member.MemberId;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

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

    @Column(name = "refresh_token", length = 2000)
    private String refreshToken;

    @Column(name = "refresh_token_expires_at")
    private LocalDateTime refreshTokenExpiresAt;

    @Builder
    public Account(final String username, final MemberId memberId) {
        this.username = username;
        this.memberId = memberId;
    }

    public Account(final String username) {
        this.username = username;
    }

    public void signup(final MemberId memberId) {
        if (getMemberId() != null) {
            throw new AccountAlreadySignupException(getUsername());
        }
        this.memberId = memberId;
    }

    public void renewRefreshToken(final String refreshToken, final LocalDateTime expiresAt) {

        Assert.notNull(Account.this.refreshToken, "refreshToken 은 null 일 수 없습니다.");
        Assert.notNull(expiresAt, "expiresAt 은 null 일 수 없습니다.");

        this.refreshToken = refreshToken;
        this.refreshTokenExpiresAt = expiresAt;
    }

    public void refreshTokenExpires() {
        this.refreshToken = null;
        this.refreshTokenExpiresAt = null;
    }

}
