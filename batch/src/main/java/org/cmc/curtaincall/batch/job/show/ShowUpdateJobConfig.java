package org.cmc.curtaincall.batch.job.show;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cmc.curtaincall.batch.service.kopis.KopisService;
import org.cmc.curtaincall.batch.service.kopis.response.ShowResponse;
import org.cmc.curtaincall.domain.show.Show;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ShowUpdateJobConfig {

    private static final String JOB_NAME = "ShowUpdateJob";

    private static final String STEP_NAME = "ShowUpdateStep";

    private static final int CHUNK_SIZE = 100;

    private final JobRepository jobRepository;

    private final KopisService kopisService;

    private final EntityManagerFactory emf;

    private final PlatformTransactionManager txManager;

    @Bean
    public Job showUpdateJob() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(showUpdateStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    @JobScope
    public Step showUpdateStep() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .<ShowResponse, Show>chunk(CHUNK_SIZE, txManager)
                .reader(showPagingItemReader(null, null))
                .processor(showItemProcessor())
                .writer(showItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public ShowPagingItemReader showPagingItemReader(
            @Value("#{jobParameters[startDate]}") String startDate,
            @Value("#{jobParameters[endDate]}") String endDate
    ) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        ShowPagingItemReader itemReader = new ShowPagingItemReader(
                kopisService,
                LocalDate.parse(startDate, formatter),
                LocalDate.parse(endDate, formatter)
        );
        itemReader.setPageSize(CHUNK_SIZE);
        return itemReader;
    }

    @Bean
    @StepScope
    public ShowItemProcessor showItemProcessor() {
        return new ShowItemProcessor(kopisService);
    }

    @Bean
    @StepScope
    public JpaItemWriter<Show> showItemWriter() {
        return new JpaItemWriterBuilder<Show>()
                .entityManagerFactory(emf)
                .build();
    }
}
