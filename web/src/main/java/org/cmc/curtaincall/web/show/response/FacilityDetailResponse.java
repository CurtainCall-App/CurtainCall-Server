package org.cmc.curtaincall.web.show.response;

import lombok.*;
import org.cmc.curtaincall.domain.show.FacilityId;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FacilityDetailResponse {

    private FacilityId id;

    private String name;

    private Integer hallNum;

    private String characteristic;

    private Integer openingYear;

    private Integer seatNum;

    private String phone;

    private String homepage;

    private String address;

    private Double latitude;

    private Double longitude;
}
