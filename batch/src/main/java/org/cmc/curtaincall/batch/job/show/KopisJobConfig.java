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
public class KopisJobConfig {

    private static final String JOB_NAME = "KopisJob";

    private final JobRepository jobRepository;

    @Bean
    public Job kopisJob() {
        final JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(facilityJobStep(null))
                .next(showCreateJobStep(null))
                .next(showKidStateJobStep(null))
                .build();
    }


    @Bean
    @JobScope
    public Step facilityJobStep(final Job facilityJob) {
        final StepBuilder stepBuilder = new StepBuilder("FacilityJobStep", jobRepository);
        return stepBuilder
                .job(facilityJob)
                .build();
    }

    @Bean
    @JobScope
    public Step showCreateJobStep(final Job showCreateJob) {
        final StepBuilder stepBuilder = new StepBuilder("ShowCreateJobStep", jobRepository);
        return stepBuilder
                .job(showCreateJob)
                .build();
    }

    @Bean
    @JobScope
    public Step showKidStateJobStep(final Job showKidStateJob) {
        final StepBuilder stepBuilder = new StepBuilder("ShowKidStateJobStep", jobRepository);
        return stepBuilder
                .job(showKidStateJob)
                .build();
    }
}
