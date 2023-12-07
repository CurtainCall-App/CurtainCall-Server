package org.cmc.curtaincall.web.report;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.web.report.request.ReportCreate;
import org.cmc.curtaincall.web.security.config.LoginMemberId;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/reports")
    public void createReport(@RequestBody @Validated ReportCreate reportCreate, @LoginMemberId MemberId memberId) {
        reportService.create(reportCreate, new CreatorId(memberId));
    }
}
