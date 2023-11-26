package org.cmc.curtaincall.web.boxoffice.infra;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@XmlRootElement(name = "boxofs")
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class KopisBoxOfficeResponseList {

    @XmlElement(name = "boxof")
    private List<KopisBoxOfficeResponse> content;
}
