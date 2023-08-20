package org.cmc.curtaincall.batch.job.boxoffice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cmc.curtaincall.batch.service.kopis.KopisService;
import org.cmc.curtaincall.batch.service.kopis.request.BoxOfficeRequest;
import org.cmc.curtaincall.domain.show.BoxOffice;
import org.cmc.curtaincall.domain.show.BoxOfficeGenre;
import org.cmc.curtaincall.domain.show.BoxOfficeType;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.repository.BoxOfficeRepository;
import org.cmc.curtaincall.domain.show.repository.ShowRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class BoxOfficeJobConfig {

    private static final String JOB_NAME = "BoxOfficeJob";

    private static final String STEP_NAME = "BoxOfficeStep";

    private final JobRepository jobRepository;

    private final KopisService kopisService;

    private final PlatformTransactionManager txManager;

    private final ShowRepository showRepository;

    private final BoxOfficeRepository boxOfficeRepository;

    @Bean
    public Job boxOfficeJob() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(boxOfficeStep(null))
                .build();
    }

    @Bean
    @JobScope
    public Step boxOfficeStep(@Value("#{jobParameters[date]}") String date) {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .tasklet((contribution, chunkContext) -> {
                    LocalDate baseDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
                    List<BoxOfficeRequest> requestParams = getBoxOfficeRequestParams(baseDate);

                    List<BoxOffice> boxOffices = new ArrayList<>();
                    for (BoxOfficeRequest requestParam : requestParams) {
                        boxOffices.addAll(getBoxOfficesOfRequest(requestParam));
                    }
                    boxOfficeRepository.saveAll(boxOffices);
                    return RepeatStatus.FINISHED;
                }, txManager)
                .build();
    }

    public List<BoxOfficeRequest> getBoxOfficeRequestParams(LocalDate baseDate) {
        List<BoxOfficeRequest> requestParams = new ArrayList<>();
        for (BoxOfficeType type : BoxOfficeType.values()) {
            for (BoxOfficeGenre genre : BoxOfficeGenre.values()) {
                requestParams.add(new BoxOfficeRequest(type, genre, baseDate));
            }
        }
        return requestParams;
    }

    private List<BoxOffice> getBoxOfficesOfRequest(BoxOfficeRequest request) {
        Set<String> allowedGenreNames = Arrays.stream(ShowGenre.values())
                .map(ShowGenre::getTitle)
                .collect(Collectors.toSet());
        return kopisService.getBoxOfficeList(request).stream()
                .filter(response -> allowedGenreNames.contains(response.genreName()))
                .map(response -> BoxOffice.builder()
                        .baseDate(request.baseDate())
                        .type(request.type())
                        .genre(request.genre())
                        .show(showRepository.getReferenceById(response.showId()))
                        .rank(response.rank())
                        .build())
                .toList();
    }

}
