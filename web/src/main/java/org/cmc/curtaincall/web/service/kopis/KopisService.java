package org.cmc.curtaincall.web.service.kopis;

import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.web.service.kopis.request.ShowBoxOfficeRequest;
import org.cmc.curtaincall.web.service.kopis.request.ShowListRequest;
import org.cmc.curtaincall.web.service.kopis.response.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.xml.Jaxb2XmlDecoder;
import org.springframework.http.codec.xml.Jaxb2XmlEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class KopisService {

    private final String serviceKey;

    private final WebClient webClient;

    private final DateTimeFormatter requestFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public KopisService(@Value("${app.kopis.service-key}") String serviceKey) {
        this.serviceKey = serviceKey;
        this.webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
                .exchangeStrategies(ExchangeStrategies.builder().codecs(configurer -> {
                    configurer.defaultCodecs().jaxb2Encoder(new Jaxb2XmlEncoder());
                    configurer.defaultCodecs().jaxb2Decoder(new Jaxb2XmlDecoder());
                }).build())
                .baseUrl("http://www.kopis.or.kr")
                .build();
    }

    public ShowDetailResponse getShowDetail(String showId) {
        return webClient.get()
                .uri(builder -> builder
                        .path("/openApi/restful/pblprfr/{showId}")
                        .queryParam("service", serviceKey)
                        .build(showId)
                )
                .retrieve()
                .bodyToMono(ShowDetailResponseWrapper.class)
                .map(ShowDetailResponseWrapper::getValue)
                .block();
    }

    public ShowBoxOfficeResponseList getBoxOffice(ShowBoxOfficeRequest request) {
        return webClient.get()
                .uri(builder -> builder
                        .path("/openApi/restful/boxoffice")
                        .queryParam("service", serviceKey)
                        .queryParam("systype", request.getType().name().toLowerCase())
                        .queryParam("date", request.getBaseDate().format(requestFormatter))
                        .queryParamIfPresent("catecode",
                                Optional.ofNullable(request.getGenre()).map(ShowGenre::getCode))
                        .build()
                )
                .retrieve()
                .bodyToMono(ShowBoxOfficeResponseList.class)
                .block();
    }

}
