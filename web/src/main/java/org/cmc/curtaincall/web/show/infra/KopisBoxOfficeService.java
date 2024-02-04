package org.cmc.curtaincall.web.show.infra;

import lombok.extern.slf4j.Slf4j;
import org.cmc.curtaincall.domain.show.BoxOfficeGenre;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.domain.show.repository.ShowRepository;
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
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
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

    public KopisBoxOfficeService(final String baseUrl, final String serviceKey, final ShowRepository showRepository) {
        this.serviceKey = serviceKey;
        this.restTemplate = new RestTemplateBuilder()
                .rootUri(baseUrl)
                .setConnectTimeout(Duration.ofMillis(4000L))
                .build();
        this.showRepository = showRepository;
    }

    private final ShowRepository showRepository;

    @Override
    @Cacheable(key = "#request")
    public List<BoxOfficeResponse> getList(final BoxOfficeRequest request) {
        final List<KopisBoxOfficeResponse> boxOfficeResponses = requestEntity(request).getContent().stream()
                .filter(response -> handledGenreName.contains(response.getGenreName()))
                .toList();
        final List<ShowId> showIds = boxOfficeResponses.stream()
                .map(KopisBoxOfficeResponse::getShowId)
                .toList();
        final Map<ShowId, Show> showIdToShow = showRepository.findAllById(showIds)
                .stream()
                .collect(Collectors.toMap(Show::getId, Function.identity()));
        return boxOfficeResponses.stream()
                .filter(boxOffice -> showIdToShow.containsKey(boxOffice.getShowId()))
                .map(boxOffice -> BoxOfficeResponse.builder()
                        .id(boxOffice.getShowId())
                        .name(showIdToShow.get(boxOffice.getShowId()).getName())
                        .startDate(showIdToShow.get(boxOffice.getShowId()).getStartDate())
                        .endDate(showIdToShow.get(boxOffice.getShowId()).getEndDate())
                        .facilityName(boxOffice.getFacilityName())
                        .poster(showIdToShow.get(boxOffice.getShowId()).getPoster())
                        .genre(showIdToShow.get(boxOffice.getShowId()).getGenre())
                        .showTimes(showIdToShow.get(boxOffice.getShowId()).getShowTimes())
                        .runtime(showIdToShow.get(boxOffice.getShowId()).getRuntime())
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
