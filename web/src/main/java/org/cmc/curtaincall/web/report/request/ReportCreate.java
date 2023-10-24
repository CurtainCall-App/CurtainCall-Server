package org.cmc.curtaincall.web.report.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.cmc.curtaincall.domain.report.ReportReason;
import org.cmc.curtaincall.domain.report.ReportType;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportCreate {

    @NotNull
    @Positive
    private Long idToReport;

    @NotNull
    private ReportType type;

    @NotNull
    private ReportReason reason;

    @Size(max = 400)
    @NotNull
    private String content;
}
