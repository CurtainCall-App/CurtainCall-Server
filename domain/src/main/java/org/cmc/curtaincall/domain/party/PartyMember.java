package org.cmc.curtaincall.domain.party;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.member.MemberId;

@Entity
@Table(name = "party_member",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_party_member__party_member",
                        columnNames = {"party_id", "member_id"}
                )
        },
        indexes = {
                @Index(name = "IX_party_member__member_party", columnList = "member_id, party_id desc")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartyMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "party_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Party party;

    @Embedded
    private MemberId memberId;

    public PartyMember(final Party party, final MemberId memberId) {
        this.party = party;
        this.memberId = memberId;
    }
}
