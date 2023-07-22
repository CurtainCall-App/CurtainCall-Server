package org.cmc.curtaincall.web.service.kopis.response;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@XmlRootElement(name = "dbs")
@ToString
public class ShowDetailResponseWrapper {

    @XmlElement(name = "db")
    private ShowDetailResponse value;
}
