package org.cmc.curtaincall.web.service.kopis.response;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@XmlRootElement(name = "boxof")
@ToString
public class ShowBoxOfficeResponse {

    @XmlElement(name = "mt20id")    // 공연 ID
    private String id;

    @XmlElement(name = "cate")      // 장르
    private String genre;

    @XmlElement(name = "rnum")      // 순위
    private Integer rank;

    @XmlElement(name = "prfnm")     // 공연명
    private String name;

    @XmlElement(name = "prfpd")     // 공연기간
    private String period;

    @XmlElement(name = "prfplcnm")  // 공연장
    private String facilityName;

    @XmlElement(name = "seatcnt")   // 좌석수
    private Integer seatCount;

    @XmlElement(name = "prfdtcnt")  // 상연횟수
    private Integer showCount;

    @XmlElement(name = "area")      // 지역
    private String area;

    @XmlElement(name = "poster")    // 포스터이미지
    private String poster;
}
