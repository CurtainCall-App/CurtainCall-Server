package org.cmc.curtaincall.batch.job.facility;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cmc.curtaincall.batch.service.kopis.KopisService;
import org.cmc.curtaincall.batch.service.kopis.response.FacilityResponse;
import org.cmc.curtaincall.domain.show.Facility;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class FacilityJobConfig {

    private static final String JOB_NAME = "FacilityJob";

    private static final String STEP_NAME = "FacilityStep";

    private static final int CHUNK_SIZE = 100;

    private final JobRepository jobRepository;

    private final KopisService kopisService;

    private final EntityManagerFactory emf;

    private final PlatformTransactionManager txManager;

    @Bean
    public Job facilityJob() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(facilityStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    @JobScope
    public Step facilityStep() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .<FacilityResponse, Facility>chunk(CHUNK_SIZE, txManager)
                .reader(facilityPagingItemReader())
                .processor(facilityItemProcessor())
                .writer(facilityJpaItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public FacilityPagingItemReader facilityPagingItemReader() {
        FacilityPagingItemReader itemReader = new FacilityPagingItemReader(kopisService);
        itemReader.setPageSize(CHUNK_SIZE);
        return itemReader;
    }

    @Bean
    @StepScope
    public FacilityItemProcessor facilityItemProcessor() {
        return new FacilityItemProcessor(kopisService, emf.createEntityManager());
    }

    @Bean
    @StepScope
    public JpaItemWriter<Facility> facilityJpaItemWriter() {
        return new JpaItemWriterBuilder<Facility>()
                .entityManagerFactory(emf)
                .build();
    }
}
