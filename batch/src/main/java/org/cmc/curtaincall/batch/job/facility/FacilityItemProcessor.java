package org.cmc.curtaincall.batch.job.facility;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.batch.service.kopis.KopisService;
import org.cmc.curtaincall.batch.service.kopis.response.FacilityDetailResponse;
import org.cmc.curtaincall.batch.service.kopis.response.FacilityResponse;
import org.cmc.curtaincall.domain.show.Facility;
import org.springframework.batch.item.ItemProcessor;

@RequiredArgsConstructor
public class FacilityItemProcessor implements ItemProcessor<FacilityResponse, Facility> {

    private final KopisService kopisService;

    @Override
    public Facility process(FacilityResponse item) throws Exception {
        FacilityDetailResponse facilityDetail = kopisService.getFacilityDetail(item.id());
        return Facility.builder()
                .id(facilityDetail.id())
                .name(facilityDetail.name())
                .hallNum(facilityDetail.hallNum())
                .characteristics(facilityDetail.characteristics())
                .openingYear(facilityDetail.openingYear())
                .seatNum(facilityDetail.seatNum())
                .phone(facilityDetail.phone())
                .homepage(facilityDetail.homepage())
                .address(facilityDetail.address())
                .sido(item.sido())
                .gugun(item.gugun())
                .latitude(facilityDetail.latitude())
                .longitude(facilityDetail.longitude())
                .build();
    }
}
