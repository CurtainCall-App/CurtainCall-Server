package org.cmc.curtaincall.web.service.kopis.response;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@XmlRootElement(name = "boxofs")
@ToString
public class ShowBoxOfficeResponseList {

    @XmlElement(name = "boxof")
    private List<ShowBoxOfficeResponse> boxOffice;
}
