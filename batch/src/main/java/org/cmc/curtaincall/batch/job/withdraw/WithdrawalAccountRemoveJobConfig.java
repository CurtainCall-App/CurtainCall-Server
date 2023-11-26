package org.cmc.curtaincall.batch.job.withdraw;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.batch.job.common.JpaRemoveItemWriter;
import org.cmc.curtaincall.batch.job.common.QuerydslPagingItemReader;
import org.cmc.curtaincall.domain.account.Account;
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

import static org.cmc.curtaincall.domain.account.QAccount.account;
import static org.cmc.curtaincall.domain.member.QMemberWithdrawal.memberWithdrawal;

@Configuration
@RequiredArgsConstructor
public class WithdrawalAccountRemoveJobConfig {

    private static final String JOB_NAME = "WithdrawalAccountRemoveJob";

    private static final String STEP_NAME = "WithdrawalAccountRemoveStep";

    private static final int CHUNK_SIZE = 100;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager txManager;

    private final EntityManagerFactory emf;

    @Bean
    public Job withdrawalAccountRemoveJob() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(withdrawalAccountRemoveStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    @JobScope
    public Step withdrawalAccountRemoveStep() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .<Account, Account>chunk(CHUNK_SIZE, txManager)
                .reader(withdrawalAccountRemoveItemReader(null))
                .writer(withdrawalAccountRemoveItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public QuerydslPagingItemReader<Account> withdrawalAccountRemoveItemReader(
            @Value("#{jobParameters[date]}") String dateParam
    ) {
        final LocalDate date = LocalDate.parse(dateParam, DateTimeFormatter.ofPattern("yyyyMMdd"));
        return new QuerydslPagingItemReader<>(emf, CHUNK_SIZE, query -> query
                .selectFrom(account)
                .join(memberWithdrawal).on(memberWithdrawal.memberId.eq(account.memberId))
                .where(
                        memberWithdrawal.useYn,
                        memberWithdrawal.createdAt.between(date.atStartOfDay(), date.atTime(23, 59, 59, 99999999))
                )
        );
    }

    @Bean
    @StepScope
    public JpaRemoveItemWriter<Account> withdrawalAccountRemoveItemWriter() {
        return new JpaRemoveItemWriter<>(emf);
    }
}
