package org.cmc.curtaincall.batch.job.show;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cmc.curtaincall.domain.show.ShowState;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ShowCompleteUpdateJobConfig {

    private static final String JOB_NAME = "ShowCompleteUpdateJob";

    private static final String STEP_NAME = "ShowCompleteUpdateStep";

    private final JobRepository jobRepository;

    private final PlatformTransactionManager txManager;

    private final EntityManager em;

    @Bean
    public Job showCompleteUpdateJob() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(showCompleteUpdateStep(null))
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    @JobScope
    public Step showCompleteUpdateStep(@Value("#{jobParameters[date]}") String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .tasklet((contribution, chunkContext) -> {
                    em.createQuery("""
                                    update Show show
                                    set show.state = :state, show.lastModifiedAt = :lastModifiedAt
                                    where show.state in :prevStates and show.endDate < :date
                                    """)
                            .setParameter("state", ShowState.COMPLETE)
                            .setParameter("prevStates", List.of(ShowState.PERFORMING, ShowState.TO_PERFORM))
                            .setParameter("lastModifiedAt", LocalDateTime.now())
                            .setParameter("date", LocalDate.parse(date, formatter))
                            .executeUpdate();

                    em.createQuery("""
                                    update ShowReviewStats stats
                                    set stats.state = :state, stats.lastModifiedAt = :lastModifiedAt
                                    where stats.state in :prevStates and stats.endDate < :date
                                    """)
                            .setParameter("state", ShowState.COMPLETE)
                            .setParameter("prevStates", List.of(ShowState.PERFORMING, ShowState.TO_PERFORM))
                            .setParameter("lastModifiedAt", LocalDateTime.now())
                            .setParameter("date", LocalDate.parse(date, formatter))
                            .executeUpdate();

                    return RepeatStatus.FINISHED;
                }, txManager)
                .build();
    }
}
