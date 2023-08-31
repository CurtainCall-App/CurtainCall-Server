package org.cmc.curtaincall.web.service.lostitem.response;

import jakarta.annotation.Nullable;
import lombok.Builder;
import org.cmc.curtaincall.domain.lostitem.LostItem;
import org.cmc.curtaincall.domain.lostitem.LostItemType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public record LostItemMyResponse(
        Long id,
        String facilityId,
        String facilityName,
        String title,
        LostItemType type,
        LocalDate foundDate,
        @Nullable
        LocalTime foundTime,
        String imageUrl,
        LocalDateTime createdAt
) {

    public static LostItemMyResponse of(LostItem lostItem) {
        return LostItemMyResponse.builder()
                .id(lostItem.getId())
                .facilityId(lostItem.getFacility().getId())
                .facilityName(lostItem.getFacility().getName())
                .title(lostItem.getTitle())
                .type(lostItem.getType())
                .foundDate(lostItem.getFoundDate())
                .foundTime(lostItem.getFoundTime())
                .imageUrl(lostItem.getImage().getUrl())
                .createdAt(lostItem.getCreatedAt())
                .build();
    }
}
