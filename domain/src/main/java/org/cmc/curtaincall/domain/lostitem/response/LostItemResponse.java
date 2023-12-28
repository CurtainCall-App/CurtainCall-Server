package org.cmc.curtaincall.domain.lostitem.response;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.cmc.curtaincall.domain.show.FacilityId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@ToString
public class LostItemResponse {
    private Long id;
    private FacilityId facilityId;
    private String facilityName;
    private String title;
    private LocalDate foundDate;
    @Nullable
    private LocalTime foundTime;
    private String imageUrl;
    private LocalDateTime createdAt;

    @Builder
    @QueryProjection
    public LostItemResponse(
            Long id,
            FacilityId facilityId,
            String facilityName,
            String title,
            LocalDate foundDate,
            @Nullable LocalTime foundTime,
            String imageUrl,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.title = title;
        this.foundDate = foundDate;
        this.foundTime = foundTime;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }
}
