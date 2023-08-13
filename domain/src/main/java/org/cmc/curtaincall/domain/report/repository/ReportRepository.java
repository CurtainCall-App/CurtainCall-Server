package org.cmc.curtaincall.domain.report.repository;

import org.cmc.curtaincall.domain.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
