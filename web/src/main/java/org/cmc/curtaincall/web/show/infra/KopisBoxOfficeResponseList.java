package org.cmc.curtaincall.web.show.infra;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@XmlRootElement(name = "boxofs")
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class KopisBoxOfficeResponseList {

    @XmlElement(name = "boxof")
    private List<KopisBoxOfficeResponse> content;

    public static KopisBoxOfficeResponseList empty() {
        final KopisBoxOfficeResponseList response = new KopisBoxOfficeResponseList();
        response.content = Collections.emptyList();
        return response;
    }
}
