package org.cmc.curtaincall.web.service.facility.response;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FacilityDetailResponse {

    private String id;

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
