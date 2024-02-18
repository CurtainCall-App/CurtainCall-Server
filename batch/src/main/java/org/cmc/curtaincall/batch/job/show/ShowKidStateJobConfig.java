package org.cmc.curtaincall.batch.job.show;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cmc.curtaincall.batch.service.kopis.KopisService;
import org.cmc.curtaincall.batch.service.kopis.request.ShowListRequest;
import org.cmc.curtaincall.batch.service.kopis.response.ShowResponse;
import org.cmc.curtaincall.domain.show.ShowId;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.builder.TaskletStepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ShowKidStateJobConfig {

    private static final String JOB_NAME = "ShowKidStateJob";

    private static final String STEP_NAME = "ShowKidStateStep";

    private final JobRepository jobRepository;

    private final KopisService kopisService;

    private final EntityManager em;

    private final PlatformTransactionManager txManager;

    @Bean
    public Job showKidStateJob() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(showKidStateStep(null, null))
                .build();
    }

    @Bean
    @JobScope
    public Step showKidStateStep(
            @Value("#{jobParameters[startDate]}") String startDate,
            @Value("#{jobParameters[endDate]}") String endDate
    ) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        final ShowListRequest showListRequest = ShowListRequest.builder()
                .page(1)
                .size(100_000)
                .startDate(LocalDate.parse(startDate, formatter))
                .endDate(LocalDate.parse(endDate, formatter))
                .kid("Y")
                .build();
        final TaskletStepBuilder stepBuilder = new TaskletStepBuilder(new StepBuilder(STEP_NAME, jobRepository));

        return stepBuilder.tasklet((contribution, chunkContext) -> {
            final List<ShowId> kidShowIds = kopisService.getShowList(showListRequest).stream()
                    .map(ShowResponse::id)
                    .map(ShowId::new)
                    .toList();

            em.createQuery("update Show s set s.kidState = true where s.id in :ids")
                    .setParameter("ids", kidShowIds)
                    .executeUpdate();

            return RepeatStatus.FINISHED;
        }, txManager).build();
    }

}
