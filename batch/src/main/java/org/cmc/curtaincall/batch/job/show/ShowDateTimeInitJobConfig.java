package org.cmc.curtaincall.batch.job.show;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.ShowDateTime;
import org.cmc.curtaincall.domain.show.ShowDay;
import org.cmc.curtaincall.domain.show.ShowTime;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ShowDateTimeInitJobConfig {

    private static final String JOB_NAME = "ShowDateTimeInitJob";

    private static final String STEP_NAME = "ShowDateTimeInitStep";

    private final JobRepository jobRepository;

    private final EntityManager em;

    private final PlatformTransactionManager txManager;

    private final Map<ShowDay, DayOfWeek> showDayToDayOfWeek = Map.of(
            ShowDay.MONDAY, DayOfWeek.MONDAY,
            ShowDay.TUESDAY, DayOfWeek.TUESDAY,
            ShowDay.WEDNESDAY, DayOfWeek.WEDNESDAY,
            ShowDay.THURSDAY, DayOfWeek.THURSDAY,
            ShowDay.FRIDAY, DayOfWeek.FRIDAY,
            ShowDay.SATURDAY, DayOfWeek.SATURDAY,
            ShowDay.SUNDAY, DayOfWeek.SUNDAY
    );

    @Bean
    public Job showDateTimeInitJob() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(showDateTimeInitStep())
                .build();
    }

    @Bean
    @JobScope
    public Step showDateTimeInitStep() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .tasklet((contribution, chunkContext) -> {
                    final int size = 10;
                    int showCount = em.createQuery("select count(show) from Show show", Long.class)
                            .getSingleResult().intValue();
                    for (int page = 0; page < showCount / size + 1; page++) {
                        List<Show> shows = em.createQuery("""
                                            select show
                                            from Show show
                                        """, Show.class)
                                .setFirstResult(page * size)
                                .setMaxResults(size)
                                .getResultList();
                        List<ShowDateTime> showDateTimes = new ArrayList<>();
                        for (Show show : shows) {
                            showDateTimes.addAll(getShowDateTimes(show));
                        }

                        for (ShowDateTime showDateTime : showDateTimes) {
                            em.persist(showDateTime);
                        }
                    }

                    return RepeatStatus.FINISHED;
                }, txManager)
                .build();
    }

    private List<ShowDateTime> getShowDateTimes(Show show) {
        Map<DayOfWeek, List<ShowTime>> dayOfWeekToShowTimes = show.getShowTimes().stream()
                .filter(showTime -> showDayToDayOfWeek.containsKey(showTime.getDayOfWeek()))
                .collect(Collectors.groupingBy(showTime -> showDayToDayOfWeek.get(showTime.getDayOfWeek())));

        LocalDate startDate = show.getStartDate();
        LocalDate endDate = show.getEndDate();
        List<ShowDateTime> result = new ArrayList<>();
        for (int i = 0; i <= Period.between(startDate, endDate).getDays(); i++) {
            LocalDate date = startDate.plusDays(i);
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            if (dayOfWeekToShowTimes.containsKey(dayOfWeek)) {
                result.addAll(dayOfWeekToShowTimes.get(dayOfWeek).stream()
                        .map(showTime -> LocalDateTime.of(date, showTime.getTime()))
                        .map(showAt -> new ShowDateTime(show, showAt))
                        .toList()
                );
            }
        }
        return result;
    }

}
