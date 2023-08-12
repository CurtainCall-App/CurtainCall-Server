package org.cmc.curtaincall.web.service.lostitem.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LostItemResponse {

    private Long id;

    private String facilityId;

    private String facilityName;

    private String title;

    private LocalDateTime foundAt;

    private String imageUrl;
}
