package org.cmc.curtaincall.web.boxoffice.infra;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.cmc.curtaincall.domain.show.ShowId;

@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class KopisBoxOfficeResponse {
    @XmlElement(name = "mt20id")
    @XmlJavaTypeAdapter(ShowIdXmlAdapter.class)
    private ShowId showId;
    @XmlElement(name = "prfnm")
    private String showName;
    @XmlElement(name = "poster")
    private String poster;
    @XmlElement(name = "prfpd")
    private String showPeriod;
    @XmlElement(name = "cate")
    private String genreName;
    @XmlElement(name = "prfdtcnt")
    private int showCount;
    @XmlElement(name = "prfplcnm")
    private String facilityName;
    @XmlElement(name = "seatcnt")
    private int seatNum;
    @XmlElement(name = "rnum")
    private int rank;
}
