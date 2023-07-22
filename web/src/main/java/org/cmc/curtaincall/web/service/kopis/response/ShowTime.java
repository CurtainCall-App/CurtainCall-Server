package org.cmc.curtaincall.web.service.kopis.response;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class ShowTime {

    private DayOfWeek dayOfWeek;

    private LocalTime time;

    @Override
    public String toString() {
        return dayOfWeek + "(" + time + ")";
    }
}
