package org.cmc.curtaincall.web.show.infra;

import lombok.extern.slf4j.Slf4j;
import org.cmc.curtaincall.domain.show.BoxOfficeGenre;
import org.cmc.curtaincall.web.show.BoxOfficeService;
import org.cmc.curtaincall.web.show.request.BoxOfficeRequest;
import org.cmc.curtaincall.web.show.response.BoxOfficeResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CacheConfig(cacheNames = "boxOffices")
@Slf4j
public class KopisBoxOfficeService implements BoxOfficeService {

    private final Set<String> handledGenreName = Arrays.stream(BoxOfficeGenre.values())
            .map(BoxOfficeGenre::getTitle)
            .collect(Collectors.toSet());
    private final DateTimeFormatter requestDateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final String serviceKey;

    private final RestTemplate restTemplate;

    public KopisBoxOfficeService(final String baseUrl, final String serviceKey) {
        this.serviceKey = serviceKey;
        this.restTemplate = new RestTemplateBuilder()
                .rootUri(baseUrl)
                .setConnectTimeout(Duration.ofMillis(4000L))
                .build();
    }

    @Override
    @Cacheable(key = "#request")
    public List<BoxOfficeResponse> getList(final BoxOfficeRequest request) {
        final List<KopisBoxOfficeResponse> boxOfficeResponses = requestEntity(request).getContent().stream()
                .filter(response -> handledGenreName.contains(response.getGenreName()))
                .toList();
        return boxOfficeResponses.stream()
                .map(boxOffice -> BoxOfficeResponse.builder()
                        .id(boxOffice.getShowId())
                        .rank(boxOffice.getRank())
                        .build()
                ).toList();
    }

    private KopisBoxOfficeResponseList requestEntity(final BoxOfficeRequest request) {
        final String uri = UriComponentsBuilder.fromPath("/openApi/restful/boxoffice")
                .queryParam("service", serviceKey)
                .queryParam("ststype", request.type().getCode().toLowerCase())
                .queryParam("date", request.baseDate().format(requestDateTimeFormatter))
                .queryParamIfPresent("catecode", Optional.ofNullable(request.genre())
                        .map(BoxOfficeGenre::getCode))
                .build()
                .toUriString();
        final ResponseEntity<KopisBoxOfficeResponseList> response = restTemplate.getForEntity(
                uri, KopisBoxOfficeResponseList.class);
        if (response.getStatusCode().is5xxServerError()) {
            return KopisBoxOfficeResponseList.empty();
        }
        final boolean emptyResponse = Optional.ofNullable(response.getBody())
                .map(KopisBoxOfficeResponseList::getContent)
                .isEmpty();
        if (emptyResponse) {
            return KopisBoxOfficeResponseList.empty();
        }
        return response.getBody();
    }

    @CacheEvict(value = "boxOffices", allEntries = true)
    @Scheduled(cron = "0 0 12 * * ?")
    public void emptyBoxOfficesCache() {
        log.info("emptying BoxOffices cache");
    }
}
