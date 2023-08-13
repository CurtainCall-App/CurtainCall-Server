package org.cmc.curtaincall.web.service.report;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.party.Party;
import org.cmc.curtaincall.domain.party.repository.PartyRepository;
import org.cmc.curtaincall.domain.report.Report;
import org.cmc.curtaincall.domain.report.repository.ReportRepository;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.report.request.ReportCreate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    private final PartyRepository partyRepository;

    public IdResult<Long> create(ReportCreate reportCreate) {
        Party party = getPartyById(reportCreate.getPartyId());
        Report report = reportRepository.save(Report.builder()
                .content(reportCreate.getContent())
                .party(party)
                .reason(reportCreate.getReason())
                .build());
        return new IdResult<>(report.getId());
    }

    private Party getPartyById(Long id) {
        return partyRepository.findById(id)
                .filter(Party::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Party ID=" + id));
    }
}
