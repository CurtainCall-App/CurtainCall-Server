package org.cmc.curtaincall.web.service.lostitem.response;

import lombok.*;
import org.cmc.curtaincall.domain.lostitem.LostItemType;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LostItemDetailResponse {

    private Long id;

    private String facilityId;

    private String facilityName;

    private String facilityPhone;

    private String title;

    private LostItemType type;

    private String foundPlaceDetail;

    private LocalDate foundDate;

    private LocalTime foundTime;

    private String particulars;

    private Long imageId;

    private String imageUrl;
}
