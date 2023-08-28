package org.cmc.curtaincall.domain.report;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseEntity;

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

    @Column(name = "reported_id", nullable = false)
    private Long reportedId;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason", length = 45, nullable = false)
    private ReportReason reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ReportType type;

    @Builder
    private Report(String content, Long reportedId, ReportReason reason, ReportType type) {
        this.content = content;
        this.reportedId = reportedId;
        this.reason = reason;
        this.type = type;
    }
}
