package org.cmc.curtaincall.domain.account;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseTimeEntity;

import java.time.LocalDateTime;
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

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "refresh_token_expires_at", nullable = false)
    private LocalDateTime refreshTokenExpiresAt;

    public Account(final String username) {
        this.username = username;
        this.memberId = null;
        this.refreshToken = "";
        this.refreshTokenExpiresAt = LocalDateTime.now();
    }

    public void renewRefreshToken(final String refreshToken, final LocalDateTime expiresAt) {
        this.refreshToken = Objects.requireNonNull(refreshToken);
        this.refreshTokenExpiresAt = Objects.requireNonNull(expiresAt);
    }

    public void refreshTokenExpires() {
        this.refreshToken = "";
        this.refreshTokenExpiresAt = LocalDateTime.now();
    }

    public void registerMember(final MemberId memberId) {
        this.memberId = Objects.requireNonNull(memberId);
    }
}
