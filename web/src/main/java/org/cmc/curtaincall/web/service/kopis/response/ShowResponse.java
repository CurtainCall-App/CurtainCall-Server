package org.cmc.curtaincall.web.service.kopis.response;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.*;
import org.cmc.curtaincall.web.service.kopis.response.adapter.KopisLocalDateAdapter;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@XmlRootElement(name = "db")
@ToString
public class ShowResponse {

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

    @XmlElement(name = "fcltynm")
    private String facility;            // 공연 시설명(공연장명)

    @XmlElement(name = "poster")
    private String poster;              // 공연 포스터 경로

    @XmlElement(name = "genrenm")
    private String genre;               // 공연 장르명

    @XmlElement(name = "prfstate")
    private String state;               // 공연 상태

    @XmlElement(name = "openrun")
    private String openRun;             // 오픈런
}
