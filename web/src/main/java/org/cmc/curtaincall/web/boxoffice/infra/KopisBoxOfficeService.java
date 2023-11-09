package org.cmc.curtaincall.web.boxoffice.infra;

import org.cmc.curtaincall.domain.show.BoxOfficeGenre;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.domain.show.repository.ShowRepository;
import org.cmc.curtaincall.web.boxoffice.BoxOfficeService;
import org.cmc.curtaincall.web.boxoffice.dto.BoxOfficeRequest;
import org.cmc.curtaincall.web.boxoffice.dto.BoxOfficeResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class KopisBoxOfficeService implements BoxOfficeService {

    private final Set<String> handledGenreName = Arrays.stream(BoxOfficeGenre.values())
            .filter(genre -> genre != BoxOfficeGenre.ALL)
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
    public List<BoxOfficeResponse> getList(final BoxOfficeRequest request) {
        final List<KopisBoxOfficeResponse> boxOfficeResponses = requestEntity(request).getContent().stream()
                .filter(response -> handledGenreName.contains(response.getGenreName()))
                .toList();
        final List<ShowId> showIds = boxOfficeResponses.stream()
                .map(KopisBoxOfficeResponse::getShowId)
                .toList();
        final Map<String, Show> showIdToShow = showRepository.findAllById(showIds.stream().map(ShowId::getId).toList())
                .stream()
                .collect(Collectors.toMap(Show::getId, Function.identity()));
        return boxOfficeResponses.stream()
                .filter(boxOffice -> showIdToShow.containsKey(boxOffice.getShowId().getId()))
                .map(boxOffice -> BoxOfficeResponse.builder()
                        .id(boxOffice.getShowId())
                        .name(showIdToShow.get(boxOffice.getShowId().getId()).getName())
                        .startDate(showIdToShow.get(boxOffice.getShowId().getId()).getStartDate())
                        .endDate(showIdToShow.get(boxOffice.getShowId().getId()).getEndDate())
                        .poster(showIdToShow.get(boxOffice.getShowId().getId()).getPoster())
                        .genre(showIdToShow.get(boxOffice.getShowId().getId()).getGenre())
                        .reviewCount(showIdToShow.get(boxOffice.getShowId().getId()).getReviewCount())
                        .reviewGradeSum(showIdToShow.get(boxOffice.getShowId().getId()).getReviewGradeSum())
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
                .queryParamIfPresent("area", Optional.ofNullable(request.areaCode()))
                .build()
                .toUriString();
        final ResponseEntity<KopisBoxOfficeResponseList> response = restTemplate.getForEntity(
                uri, KopisBoxOfficeResponseList.class);
        return Objects.requireNonNull(response.getBody());
    }
}
