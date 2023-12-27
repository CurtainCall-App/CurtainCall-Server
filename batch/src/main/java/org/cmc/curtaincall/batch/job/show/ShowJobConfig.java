package org.cmc.curtaincall.batch.job.show;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ShowJobConfig {

    private static final String JOB_NAME = "ShowJob";

    private final JobRepository jobRepository;

    @Bean
    public Job showJob() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(facilityJobStep(null))
                .next(showCreateJobStep(null))
                .next(showPerformingUpdateJobStep(null))
                .next(showCompleteUpdateJobStep(null))
                .build();
    }

    @Bean
    @JobScope
    public Step facilityJobStep(final Job facilityJob) {
        StepBuilder stepBuilder = new StepBuilder("FacilityJobStep", jobRepository);
        return stepBuilder
                .job(facilityJob)
                .build();
    }

    @Bean
    @JobScope
    public Step showCreateJobStep(final Job showCreateJob) {
        StepBuilder stepBuilder = new StepBuilder("ShowCreateJobStep", jobRepository);
        return stepBuilder
                .job(showCreateJob)
                .build();
    }

    @Bean
    @JobScope
    public Step showPerformingUpdateJobStep(final Job showPerformingUpdateJob) {
        StepBuilder stepBuilder = new StepBuilder("ShowPerformingUpdateJobStep", jobRepository);
        return stepBuilder
                .job(showPerformingUpdateJob)
                .build();
    }

    @Bean
    @JobScope
    public Step showCompleteUpdateJobStep(final Job showCompleteUpdateJob) {
        StepBuilder stepBuilder = new StepBuilder("ShowCompleteUpdateJobStep", jobRepository);
        return stepBuilder
                .job(showCompleteUpdateJob)
                .build();
    }
}
