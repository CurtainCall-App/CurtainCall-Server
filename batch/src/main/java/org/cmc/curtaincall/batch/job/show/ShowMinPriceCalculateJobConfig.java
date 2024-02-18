package org.cmc.curtaincall.batch.job.show;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cmc.curtaincall.domain.show.Show;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.builder.TaskletStepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ShowMinPriceCalculateJobConfig {

    private static final String JOB_NAME = " ";

    private static final String STEP_NAME = "ShowMinPriceCalculateStep";

    private final JobRepository jobRepository;

    private final EntityManager em;

    private final PlatformTransactionManager txManager;

    @Bean
    public Job showMinPriceCalculateJob() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(showMinPriceCalculateStep())
                .build();
    }

    @Bean
    @JobScope
    public Step showMinPriceCalculateStep() {
        final TaskletStepBuilder stepBuilder = new TaskletStepBuilder(new StepBuilder(STEP_NAME, jobRepository));

        return stepBuilder.tasklet((contribution, chunkContext) -> {
            em.createQuery("select s from Show s", Show.class).getResultList().forEach(show -> {
                final String ticketPrice = show.getTicketPrice();
                final int minTicketPrice = extractMinTicketPrice(ticketPrice);
                em.createQuery("update Show s set s.minTicketPrice = :minTicketPrice where s.id = :id")
                        .setParameter("minTicketPrice", minTicketPrice)
                        .setParameter("id", show.getId())
                        .executeUpdate();
            });

            return RepeatStatus.FINISHED;
        }, txManager).build();
    }

    private int extractMinTicketPrice(String ticketPrice) {
        List<String> prices = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d{1,3}(,\\d{3})*원", Pattern.CANON_EQ);
        Matcher matcher = pattern.matcher(ticketPrice);
        while (matcher.find()) {
            prices.add(matcher.group());
        }
        return prices.stream()
                .map(s -> s.replaceAll("[^0-9]", ""))
                .mapToInt(Integer::parseInt)
                .min()
                .orElse(0);
    }

}
