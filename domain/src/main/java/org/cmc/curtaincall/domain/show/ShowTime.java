package org.cmc.curtaincall.domain.show;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Embeddable
@Getter
@EqualsAndHashCode
public class ShowTime {

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", length = 25, nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(name = "time", nullable = false)
    private LocalTime time;
}
