package org.cmc.curtaincall.web.service.kopis;

import org.cmc.curtaincall.web.service.kopis.request.ShowListRequest;
import org.cmc.curtaincall.web.service.kopis.response.ShowListResponse;
import org.cmc.curtaincall.web.service.kopis.response.ShowResponse;
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

    public Slice<ShowResponse> getShows(ShowListRequest request, Pageable pageable) {
        ShowListResponse response = webClient.get()
                .uri(builder -> builder
                        .path("/openApi/restful/pblprfr")
                        .queryParam("service", serviceKey)
                        .queryParamIfPresent("name", Optional.ofNullable(request.getName()))
                        .queryParam("stdate", request.getStartDate().format(requestFormatter))
                        .queryParam("eddate", request.getEndDate().format(requestFormatter))
                        .queryParam("shcate", request.getGenre().getCode())
                        .queryParam("cpage", pageable.getPageNumber())
                        .queryParam("rows", pageable.getPageSize())
                        .build()
                )
                .retrieve()
                .bodyToMono(ShowListResponse.class)
                .block();

        if (response == null || response.getShows() == null) {
            return new SliceImpl<>(Collections.emptyList());
        }

        List<ShowResponse> performances = response.getShows();
        boolean hasNext = performances.size() == pageable.getPageSize();

        return new SliceImpl<>(performances, pageable, hasNext);
    }

}
