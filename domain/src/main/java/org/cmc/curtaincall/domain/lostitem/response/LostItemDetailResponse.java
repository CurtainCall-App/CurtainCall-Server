package org.cmc.curtaincall.domain.lostitem.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import org.cmc.curtaincall.domain.lostitem.LostItemType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@ToString
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
    private LocalDateTime createdAt;

    @Builder
    @QueryProjection
    public LostItemDetailResponse(
            long id,
            String facilityId,
            String facilityName,
            String facilityPhone,
            String title,
            LostItemType type,
            String foundPlaceDetail,
            LocalDate foundDate,
            LocalTime foundTime,
            String particulars,
            long imageId,
            String imageUrl,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.facilityPhone = facilityPhone;
        this.title = title;
        this.type = type;
        this.foundPlaceDetail = foundPlaceDetail;
        this.foundDate = foundDate;
        this.foundTime = foundTime;
        this.particulars = particulars;
        this.imageId = imageId;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }
}
