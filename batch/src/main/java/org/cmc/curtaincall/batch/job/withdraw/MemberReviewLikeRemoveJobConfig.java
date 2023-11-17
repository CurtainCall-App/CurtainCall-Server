package org.cmc.curtaincall.batch.job.withdraw;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.batch.job.common.JpaQueryUpdateItemWriter;
import org.cmc.curtaincall.batch.job.common.QuerydslPagingItemReader;
import org.cmc.curtaincall.domain.review.ShowReview;
import org.cmc.curtaincall.domain.review.ShowReviewLike;
import org.cmc.curtaincall.domain.review.repository.ShowReviewLikeRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.cmc.curtaincall.domain.member.QMemberWithdrawal.memberWithdrawal;
import static org.cmc.curtaincall.domain.review.QShowReviewLike.showReviewLike;

@Configuration
@RequiredArgsConstructor
public class MemberReviewLikeRemoveJobConfig {

    private static final String JOB_NAME = "MemberShowReviewLikeRemoveJob";

    private static final String STEP_NAME = "MemberShowReviewLikeRemoveStep";

    private static final int CHUNK_SIZE = 100;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager txManager;

    private final EntityManagerFactory emf;

    private final ShowReviewLikeRepository showReviewLikeRepository;

    @Bean
    public Job memberShowReviewLikeRemoveJob() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(memberShowReviewLikeRemoveStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    @JobScope
    public Step memberShowReviewLikeRemoveStep() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .<ShowReviewLike, ShowReviewLike>chunk(CHUNK_SIZE, txManager)
                .reader(memberShowReviewLikeRemoveItemReader(null))
                .writer(memberShowReviewLikeRemoveItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public QuerydslPagingItemReader<ShowReviewLike> memberShowReviewLikeRemoveItemReader(
            @Value("#{jobParameters[date]}") String dateParam
    ) {
        final LocalDate date = LocalDate.parse(dateParam, DateTimeFormatter.ofPattern("yyyyMMdd"));
        return new QuerydslPagingItemReader<>(emf, CHUNK_SIZE, query -> query
                .selectFrom(showReviewLike)
                .join(memberWithdrawal).on(memberWithdrawal.memberId.eq(showReviewLike.memberId))
                .where(
                        memberWithdrawal.useYn,
                        memberWithdrawal.createdAt.between(date.atStartOfDay(), date.atTime(23, 59, 59, 99999999))
                )
        );
    }

    @Bean
    @StepScope
    public JpaQueryUpdateItemWriter<ShowReviewLike, ShowReview> memberShowReviewLikeRemoveItemWriter() {
        return new JpaQueryUpdateItemWriter<>(emf,
                (em, like) -> em.find(ShowReview.class,
                        like.getShowReview().getId(), LockModeType.PESSIMISTIC_FORCE_INCREMENT
                ),
                (review, like) -> review.cancelLike(like.getMemberId(), showReviewLikeRepository)
        );
    }
}
