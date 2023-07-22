package org.cmc.curtaincall.web.service.kopis.response;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.*;
import org.cmc.curtaincall.web.service.kopis.response.adapter.KopisCastsAdapter;
import org.cmc.curtaincall.web.service.kopis.response.adapter.KopisLocalDateAdapter;
import org.cmc.curtaincall.web.service.kopis.response.adapter.KopisShowTimesAdapter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@XmlRootElement(name = "db")
@ToString
public class ShowDetailResponse {

    @XmlElement(name = "mt20id")
    private String id;                  // 공연 ID

    @XmlElement(name = "prfnm")
    private String name;                // 공연명

    @XmlElement(name = "prfpdfrom")
    @XmlJavaTypeAdapter(KopisLocalDateAdapter.class)
    private LocalDate startDate;        // 공연 시작일

    @XmlElement(name = "prfpdto")
    @XmlJavaTypeAdapter(KopisLocalDateAdapter.class)
    private LocalDate endDate;          // 공연 종료일

    @XmlElement(name = "mt10id")
    private String facilityId;          // 공연 시설 ID

    @XmlElement(name = "fcltynm")
    private String facilityName;            // 공연 시설명 (공연장명)

    @XmlElement(name = "prfcrew")
    private String crew;              // 공연 제작진

    @XmlElement(name = "prfcast")
    @XmlJavaTypeAdapter(KopisCastsAdapter.class)
    private List<String> casts;              // 공연 출연진

    @XmlElement(name = "prfruntime")
    private String runtime;              // 공연 런타임

    @XmlElement(name = "prfage")
    private String age;              // 공연 관람 연령

    @XmlElement(name = "entrpsnm")
    private String enterprise;              // 제작사

    @XmlElement(name = "pcseguidance")
    private String ticketPrice;              // 티켓 가격

    @XmlElement(name = "poster")
    private String poster;              // 공연 포스터 경로

    @XmlElement(name = "sty")
    private String story;               // 줄거리

    @XmlElement(name = "genrenm")
    private String genre;               // 공연 장르명

    @XmlElement(name = "prfstate")
    private String state;               // 공연 상태

    @XmlElement(name = "openrun")
    private String openRun;             // 오픈런

    @XmlElementWrapper(name = "styurls")
    @XmlElement(name = "styurl")
    private List<String> introductionImages;    // 소개 이미지

    @XmlElement(name = "dtguidance")
    @XmlJavaTypeAdapter(KopisShowTimesAdapter.class)
    private List<ShowTime> showTimes;             // 공연 시간
}
