package org.cmc.curtaincall.batch.job.withdraw;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.review.ShowReviewLike;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
@RequiredArgsConstructor
public class MemberReviewLikeRemoveJobConfig {

    private static final String JOB_NAME = "MemberShowReviewLikeRemoveJob";

    private static final String STEP_NAME = "MemberShowReviewLikeRemoveStep";

    private static final int CHUNK_SIZE = 100;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager txManager;

    private final EntityManager em;

    private final EntityManagerFactory emf;

    @Bean
    @JobScope
    public Step memberShowReviewLikeRemoveStep() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .<ShowReviewLike, ShowReviewLike>chunk(CHUNK_SIZE, txManager)
//                .reader()
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<ShowReviewLike> memberShowReviewLikeRemoveItemReader(
            @Value("#{jobParameters[date]}") String dateParam
    ) {
        final LocalDate date = LocalDate.parse(dateParam, DateTimeFormatter.ofPattern("yyyyMMdd"));
        return new JpaPagingItemReaderBuilder<ShowReviewLike>()
                .name("memberShowReviewLikeRemoveItemReader")
                .entityManagerFactory(emf)
                .pageSize(CHUNK_SIZE)
                .queryString("""
                        select showReviewLike
                        from ShowReviewLike showReviewLike
                        join 
                        """)
                .build();
    }
}
