package org.cmc.curtaincall.web.service.lostitem.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LostItemResponse {

    private Long id;

    private String facilityId;

    private String facilityName;

    private String title;

    private LocalDate foundDate;

    private LocalTime foundTime;

    private String imageUrl;
}
