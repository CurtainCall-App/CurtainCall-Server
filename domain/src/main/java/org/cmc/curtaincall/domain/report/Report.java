package org.cmc.curtaincall.domain.report;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseEntity;
import org.cmc.curtaincall.domain.party.Party;

@Entity
@Table(name = "report")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @Column(name = "content", length = 1000, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "party_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Party party;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason", length = 45, nullable = false)
    private ReportReason reason;

    @Builder
    private Report(String content, Party party, ReportReason reason) {
        this.content = content;
        this.party = party;
        this.reason = reason;
    }
}
