package org.cmc.curtaincall.web.controller;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.web.service.report.ReportService;
import org.cmc.curtaincall.web.service.report.request.ReportCreate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/reports")
    public void createReport(@RequestBody @Validated ReportCreate reportCreate) {
        reportService.create(reportCreate);
    }
}
