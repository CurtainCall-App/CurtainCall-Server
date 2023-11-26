package org.cmc.curtaincall.web.report;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.report.Report;
import org.cmc.curtaincall.domain.report.repository.ReportRepository;
import org.cmc.curtaincall.web.common.response.IdResult;
import org.cmc.curtaincall.web.report.request.ReportCreate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final ReportRepository reportRepository;

    @Transactional
    public IdResult<Long> create(ReportCreate reportCreate) {
        Report report = reportRepository.save(Report.builder()
                .content(reportCreate.getContent())
                .reportedId(reportCreate.getIdToReport())
                .type(reportCreate.getType())
                .reason(reportCreate.getReason())
                .build());
        return new IdResult<>(report.getId());
    }
}
