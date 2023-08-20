package org.cmc.curtaincall.batch.job.show;

import org.cmc.curtaincall.domain.show.ShowDay;
import org.cmc.curtaincall.domain.show.ShowTime;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ShowTimeParser {

    private final Map<String, ShowDay> showDayNameToValue = Arrays.stream(ShowDay.values())
            .collect(Collectors.toMap(ShowDay::getTitle, Function.identity()));

    public List<ShowTime> parse(String v) {
        if (!StringUtils.hasText(v)) {
            return Collections.emptyList();
        }
        return Arrays.stream(v
                        .trim()
                        .split("\\),\\s*"))
                .flatMap(s -> {
                    int idx = s.indexOf("(");
                    String dayStr = s.substring(0, idx);
                    List<ShowDay> dayOfWeeks = parseShowDays(dayStr);

                    String timesStr = s.substring(idx + 1);
                    List<LocalTime> times = parseTimes(timesStr);

                    return dayOfWeeks.stream()
                            .flatMap(day ->
                                    times.stream().map(time -> new ShowTime(day, time))
                            );
                })
                .toList();
    }

    private List<ShowDay> parseShowDays(String dayOfWeeksStr) {
        String[] dayOfWeeksSplit = dayOfWeeksStr.split("\\s?~\\s?");
        if (dayOfWeeksSplit.length == 1) {
            return Collections.singletonList(showDayNameToValue.get(dayOfWeeksSplit[0]));
        }

        List<ShowDay> result = new ArrayList<>();
        List<ShowDay> showDays = Arrays.stream(ShowDay.values()).toList();
        int startIdx = showDays.indexOf(showDayNameToValue.get(dayOfWeeksSplit[0]));
        int endIdx = showDays.indexOf(showDayNameToValue.get(dayOfWeeksSplit[1]));

        for (int i = startIdx; i < endIdx + 1; i++) {
            result.add(showDays.get(i));
        }

        return result;
    }

    private List<LocalTime> parseTimes(String timesStr) {
        return Arrays.stream(timesStr.replaceAll("\\)$", "")
                        .split("\\s*,\\s*"))
                .map(LocalTime::parse)
                .toList();
    }
}
