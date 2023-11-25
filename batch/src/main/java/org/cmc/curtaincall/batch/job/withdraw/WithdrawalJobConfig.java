package org.cmc.curtaincall.batch.job.withdraw;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WithdrawalJobConfig {

    private static final String JOB_NAME = "WithdrawalJob";

    private static final String STEP_NAME = "WithdrawalStep";

    private static final int CHUNK_SIZE = 100;

    private final JobRepository jobRepository;

    @Bean
    public Job withdrawalJob() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .incrementer(new RunIdIncrementer())
                .start(withdrawalShowReviewLikeRemoveJobStep(null))
                .next(withdrawalShowReviewRemoveJobStep(null))
                .next(withdrawalPartyRemoveJobStep(null))
                .next(withdrawalLostItemRemoveJobStep(null))
                .next(withdrawalMemberRemoveJobStep(null))
                .next(withdrawalAccountRemoveJobStep(null))
                .next(withdrawalEndJobStep(null))
                .build();
    }

    @Bean
    @JobScope
    public Step withdrawalShowReviewLikeRemoveJobStep(final Job withdrawalShowReviewLikeRemoveJob) {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .job(withdrawalShowReviewLikeRemoveJob)
                .build();
    }

    @Bean
    @JobScope
    public Step withdrawalShowReviewRemoveJobStep(final Job withdrawalShowReviewRemoveJob) {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .job(withdrawalShowReviewRemoveJob)
                .build();
    }

    @Bean
    @JobScope
    public Step withdrawalPartyRemoveJobStep(final Job withdrawalPartyRemoveJob) {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .job(withdrawalPartyRemoveJob)
                .build();
    }

    @Bean
    @JobScope
    public Step withdrawalLostItemRemoveJobStep(final Job withdrawalLostItemRemoveJob) {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .job(withdrawalLostItemRemoveJob)
                .build();
    }

    @Bean
    @JobScope
    public Step withdrawalMemberRemoveJobStep(final Job withdrawalMemberRemoveJob) {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .job(withdrawalMemberRemoveJob)
                .build();
    }

    @Bean
    @JobScope
    public Step withdrawalAccountRemoveJobStep(final Job withdrawalAccountRemoveJob) {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .job(withdrawalAccountRemoveJob)
                .build();
    }

    @Bean
    @JobScope
    public Step withdrawalEndJobStep(final Job withdrawalEndJob) {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .job(withdrawalEndJob)
                .build();
    }

}
