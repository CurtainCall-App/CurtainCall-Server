package org.cmc.curtaincall.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseTimeEntity;

@Entity
@Table(name = "member_withdrawal",
        indexes = {
                @Index(name = "IX_member_withdrawal__use_yn_created_at", columnList = "use_yn,created_at")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberWithdrawal extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_withdrawal_id")
    private Long id;

    @Embedded
    private MemberId memberId;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason", length = 45, nullable = false)
    private MemberWithdrawReason reason;

    @Column(name = "content", length = 500, nullable = false)
    private String content;

    @Builder
    public MemberWithdrawal(final MemberId memberId, final MemberWithdrawReason reason, final String content) {
        this.memberId = memberId;
        this.reason = reason;
        this.content = content;
    }
}
