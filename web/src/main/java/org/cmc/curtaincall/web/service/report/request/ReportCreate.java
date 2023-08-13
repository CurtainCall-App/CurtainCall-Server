package org.cmc.curtaincall.web.service.report.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.cmc.curtaincall.domain.report.ReportReason;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportCreate {

    @NotNull
    @Positive
    private Long partyId;

    @NotNull
    private ReportReason reason;

    @Size(max = 400)
    @NotNull
    private String content;
}
