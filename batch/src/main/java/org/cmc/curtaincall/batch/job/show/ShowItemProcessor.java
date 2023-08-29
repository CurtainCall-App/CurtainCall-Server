package org.cmc.curtaincall.batch.job.show;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.batch.service.kopis.KopisService;
import org.cmc.curtaincall.batch.service.kopis.response.ShowDetailResponse;
import org.cmc.curtaincall.batch.service.kopis.response.ShowResponse;
import org.cmc.curtaincall.domain.show.*;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ShowItemProcessor implements ItemProcessor<ShowResponse, Show> {

    private final KopisService kopisService;

    private final Set<String> allowedGenreNames = Arrays.stream(ShowGenre.values())
            .map(ShowGenre::getTitle)
            .collect(Collectors.toSet());

    private final Map<String, ShowGenre> genreNameToValue = Arrays.stream(ShowGenre.values())
            .collect(Collectors.toMap(ShowGenre::getTitle, Function.identity()));

    private final DateTimeFormatter showDateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    private final ShowTimeParser showTimeParser = new ShowTimeParser();

    private final Map<String, ShowState> stateMapper = Arrays.stream(ShowState.values())
            .collect(Collectors.toMap(ShowState::getTitle, Function.identity()));

    @Override
    public Show process(ShowResponse item) throws Exception {
        if (!allowedGenreNames.contains(item.genreName())) {
            return null;
        }

        ShowGenre showGenre = genreNameToValue.get(item.genreName());
        ShowDetailResponse showDetail = kopisService.getShowDetail(item.id());
        List<ShowTime> showTimes = showTimeParser.parse(showDetail.showTimes());

        Show show = Show.builder()
                .id(showDetail.id())
                .facility(new Facility(showDetail.facilityId()))
                .name(showDetail.name())
                .startDate(LocalDate.parse(showDetail.startDate(), showDateFormatter))
                .endDate(LocalDate.parse(showDetail.endDate(), showDateFormatter))
                .crew(showDetail.crew())
                .cast(showDetail.cast())
                .runtime(showDetail.runtime())
                .age(showDetail.age())
                .enterprise(showDetail.enterprise())
                .ticketPrice(showDetail.price())
                .poster(showDetail.poster())
                .story(showDetail.story())
                .genre(showGenre)
                .state(stateMapper.get(showDetail.state()))
                .openRun(showDetail.openRun())
                .build();
        show.getShowTimes().addAll(showTimes);
        show.getIntroductionImages().addAll(showDetail.introductionImages());
        return show;
    }
}
