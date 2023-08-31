package org.cmc.curtaincall.batch.job.show;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cmc.curtaincall.batch.job.common.WithPresent;
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
public class ShowCreateJobConfig {

    private static final String JOB_NAME = "ShowCreateJob";

    private static final String STEP_NAME = "ShowCreateStep";

    private static final int CHUNK_SIZE = 100;

    private final JobRepository jobRepository;

    private final KopisService kopisService;

    private final EntityManagerFactory emf;

    private final EntityManager em;

    private final PlatformTransactionManager txManager;

    @Bean
    public Job showCreateJob() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(showCreateStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    @JobScope
    public Step showCreateStep() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .<WithPresent<ShowResponse>, Show>chunk(CHUNK_SIZE, txManager)
                .reader(showKopisPagingItemReader(null, null))
                .processor(showKopisItemProcessor())
                .writer(showItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public ShowKopisPagingItemReader showKopisPagingItemReader(
            @Value("#{jobParameters[startDate]}") String startDate,
            @Value("#{jobParameters[endDate]}") String endDate
    ) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        ShowKopisPagingItemReader itemReader = new ShowKopisPagingItemReader(
                kopisService,
                LocalDate.parse(startDate, formatter),
                LocalDate.parse(endDate, formatter),
                em
        );
        itemReader.setPageSize(CHUNK_SIZE);
        return itemReader;
    }

    @Bean
    @StepScope
    public ShowKopisItemProcessor showKopisItemProcessor() {
        return new ShowKopisItemProcessor(kopisService);
    }

    @Bean
    @StepScope
    public JpaItemWriter<Show> showItemWriter() {
        return new JpaItemWriterBuilder<Show>()
                .entityManagerFactory(emf)
                .build();
    }
}
