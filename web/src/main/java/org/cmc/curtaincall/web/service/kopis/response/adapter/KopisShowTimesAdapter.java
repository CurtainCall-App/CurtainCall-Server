package org.cmc.curtaincall.web.service.kopis.response.adapter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import org.cmc.curtaincall.web.service.kopis.response.ShowTime;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KopisShowTimesAdapter extends XmlAdapter<String, List<ShowTime>> {

    private static final Map<String, DayOfWeek> dayOfWeekMap = Map.of(
            "월요일", DayOfWeek.MONDAY,
            "화요일", DayOfWeek.TUESDAY,
            "수요일", DayOfWeek.WEDNESDAY,
            "목요일", DayOfWeek.THURSDAY,
            "금요일", DayOfWeek.FRIDAY,
            "토요일", DayOfWeek.SATURDAY,
            "일요일", DayOfWeek.SUNDAY
    );

    @Override
    public List<ShowTime> unmarshal(String v) throws Exception {
        return Arrays.stream(v
                .trim()
                .split("\\),\\s*"))
                .flatMap(s -> {
                    int idx = s.indexOf("(");
                    String dayOfWeeksStr = s.substring(0, idx);
                    List<DayOfWeek> dayOfWeeks = parseDayOfWeeks(dayOfWeeksStr);

                    String timesStr = s.substring(idx + 1);
                    List<LocalTime> times = parseTimes(timesStr);

                    return dayOfWeeks.stream()
                            .flatMap(dayOfWeek ->
                                    times.stream().map(time -> new ShowTime(dayOfWeek, time))
                            );
                })
                .toList();
    }

    private List<DayOfWeek> parseDayOfWeeks(String dayOfWeeksStr) {
        String[] dayOfWeeksSplit = dayOfWeeksStr.split("\\s?~\\s?");
        List<DayOfWeek> result = new ArrayList<>();
        result.add(dayOfWeekMap.get(dayOfWeeksSplit[0]));

        if (dayOfWeeksSplit.length == 1) {
            return result;
        }

        for (int i = result.get(0).getValue() + 1; i <= dayOfWeekMap.get(dayOfWeeksSplit[1]).getValue(); i++) {
            result.add(DayOfWeek.of(i));
        }

        return result;
    }

    private List<LocalTime> parseTimes(String timesStr) {
        return Arrays.stream(timesStr.replaceAll("\\)$", "")
                        .split("\\s*,\\s*"))
                .map(LocalTime::parse)
                .toList();
    }

    @Override
    public String marshal(List<ShowTime> v) throws Exception {
        return v.stream()
                .map(ShowTime::toString)
                .collect(Collectors.joining(", "));
    }
}
