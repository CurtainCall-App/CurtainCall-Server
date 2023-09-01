package org.cmc.curtaincall.batch.job.facility;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cmc.curtaincall.batch.service.kopis.KopisService;
import org.cmc.curtaincall.batch.service.kopis.response.FacilityDetailResponse;
import org.cmc.curtaincall.batch.service.kopis.response.FacilityResponse;
import org.cmc.curtaincall.domain.show.Facility;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
@RequiredArgsConstructor
public class FacilityItemProcessor implements ItemProcessor<FacilityResponse, Facility> {

    private final KopisService kopisService;

    private final EntityManager em;

    @Override
    public Facility process(FacilityResponse item) throws Exception {
        if (isFacilityExisting(item.id())) {
            log.debug("공연시설({})은 존재하는 데이터입니다.", item.id());
            return null;
        }
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

    private boolean isFacilityExisting(String id) {
        return !em.createQuery("""
                            select 1
                            from Facility facility
                            where facility.id = :id
                        """, Integer.class)
                .setParameter("id", id)
                .getResultList()
                .isEmpty();
    }
}
