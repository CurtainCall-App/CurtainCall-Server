package org.cmc.curtaincall.batch.job.chat;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cmc.curtaincall.domain.show.ShowDateTime;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ShowChatOpenJobConfig {

    private static final String JOB_NAME = "ShowChatOpenJob";

    private static final String STEP_NAME = "ShowChatOpenStep";

    private static final int CHUNK_SIZE = 100;

    private final JobRepository jobRepository;

    private final EntityManagerFactory emf;

    private final PlatformTransactionManager txManager;

    @Bean
    public Job showChatOpenJob() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(showChatOpenStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    @JobScope
    public Step showChatOpenStep() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .<ShowDateTime, ShowDateTime>chunk(CHUNK_SIZE, txManager)
                .reader(showChatOpenItemReader(null))
                .writer(showChatOpenItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<ShowDateTime> showChatOpenItemReader(@Value("#{jobParameters[date]}") String dateParam) {
        LocalDate date = LocalDate.parse(dateParam, DateTimeFormatter.ofPattern("yyyyMMdd"));
        return new JpaPagingItemReaderBuilder<ShowDateTime>()
                .name("showChatOpenItemReader")
                .queryString("""
                        select showDateTime
                        from ShowDateTime showDateTime
                        join fetch showDateTime.show
                        where showDateTime between :startDateTime and :endDateTime
                        """)
                .parameterValues(Map.of(
                        "startDateTime", LocalDateTime.of(date, LocalTime.MIN),
                        "endDateTime", LocalDateTime.of(date, LocalTime.MAX)
                ))
                .pageSize(CHUNK_SIZE)
                .entityManagerFactory(emf)
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<ShowDateTime> showChatOpenItemWriter() {
        return items -> {
            if (items.isEmpty()) {
                return;
            }
            for (ShowDateTime item : items) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmm");
                String chatName = item.getShow().getName() + "-" + item.getShowAt().format(formatter);
                String chatId = item.getShow().getId() + "-" + item.getShowAt().format(formatter);

            }
        };
    }
}
