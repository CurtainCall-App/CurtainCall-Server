package org.cmc.curtaincall.domain.lostitem.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.cmc.curtaincall.domain.lostitem.LostItemType;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class LostItemQueryParam {

    private String facilityId;

    private LostItemType type;

    private LocalDate foundDate;

    private String title;
}
