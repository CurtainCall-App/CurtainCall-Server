package org.cmc.curtaincall.batch.job.show;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cmc.curtaincall.batch.job.common.WithPresent;
import org.cmc.curtaincall.batch.service.kopis.KopisService;
import org.cmc.curtaincall.batch.service.kopis.response.ShowDetailResponse;
import org.cmc.curtaincall.batch.service.kopis.response.ShowResponse;
import org.cmc.curtaincall.domain.show.*;
import org.springframework.batch.item.ItemProcessor;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class ShowKopisItemProcessor implements ItemProcessor<WithPresent<ShowResponse>, Show> {

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

    private final Map<ShowDay, DayOfWeek> showDayToDayOfWeek = Map.of(
            ShowDay.MONDAY, DayOfWeek.MONDAY,
            ShowDay.TUESDAY, DayOfWeek.TUESDAY,
            ShowDay.WEDNESDAY, DayOfWeek.WEDNESDAY,
            ShowDay.THURSDAY, DayOfWeek.THURSDAY,
            ShowDay.FRIDAY, DayOfWeek.FRIDAY,
            ShowDay.SATURDAY, DayOfWeek.SATURDAY,
            ShowDay.SUNDAY, DayOfWeek.SUNDAY
    );

    @Override
    public Show process(WithPresent<ShowResponse> item) throws Exception {
        if (item.present()) {
            log.debug("공연({})은 존재하는 데이터입니다.", item.value().id());
            return null;
        }
        ShowResponse showResponse = item.value();
        if (!allowedGenreNames.contains(showResponse.genreName())) {
            log.debug("공연({})은 다루지 않는 장르({})입니다.", showResponse.id(), showResponse.genreName());
            return null;
        }

        ShowGenre showGenre = genreNameToValue.get(showResponse.genreName());
        ShowDetailResponse showDetail = kopisService.getShowDetail(showResponse.id());
        List<ShowTime> showTimes = showTimeParser.parse(showDetail.showTimes());
        LocalDate startDate = LocalDate.parse(showDetail.startDate(), showDateFormatter);
        LocalDate endDate = LocalDate.parse(showDetail.endDate(), showDateFormatter);
        List<LocalDateTime> showDateTimes = getShowDateTimes(startDate, endDate, showTimes);

        Show show = Show.builder()
                .id(new ShowId(showDetail.id()))
                .facility(new Facility(showDetail.facilityId()))
                .name(showDetail.name())
                .startDate(startDate)
                .endDate(endDate)
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
        showDateTimes.forEach(show::addShowDateTime);
        return show;
    }

    private List<LocalDateTime> getShowDateTimes(LocalDate startDate, LocalDate endDate, List<ShowTime> showTimes) {
        Map<DayOfWeek, List<ShowTime>> dayOfWeekToShowTimes = showTimes.stream()
                .filter(showTime -> showDayToDayOfWeek.containsKey(showTime.getDayOfWeek()))
                .collect(Collectors.groupingBy(showTime -> showDayToDayOfWeek.get(showTime.getDayOfWeek())));

        List<LocalDateTime> result = new ArrayList<>();
        for (int i = 0; i < Period.between(startDate, endDate).getDays(); i++) {
            LocalDate date = startDate.plusDays(i);
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            if (dayOfWeekToShowTimes.containsKey(dayOfWeek)) {
                result.addAll(dayOfWeekToShowTimes.get(dayOfWeek).stream()
                        .map(showTime -> LocalDateTime.of(date, showTime.getTime()))
                        .toList()
                );
            }
        }
        return result;
    }
}
