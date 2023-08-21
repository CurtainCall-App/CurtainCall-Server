package org.cmc.curtaincall.domain.lostitem;

import lombok.Builder;
import org.cmc.curtaincall.domain.image.Image;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record LostItemEditor(
        Image image,
        String title,
        LostItemType type,
        String foundPlaceDetail,
        LocalDate foundDate,
        LocalTime foundTime,
        String particulars
) {
}
