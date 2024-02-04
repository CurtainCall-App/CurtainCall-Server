package org.cmc.curtaincall.web.recommend;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.recommend.repository.ShowRecommendationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShowRecommendationService {

    private final ShowRecommendationRepository showRecommendationRepository;

    public List<ShowRecommendationResponse> getList() {
        return showRecommendationRepository.findAllByUseYnIsTrue().stream()
                .map(showRecommendation -> ShowRecommendationResponse.builder()
                        .id(showRecommendation.getId())
                        .description(showRecommendation.getDescription())
                        .showId(showRecommendation.getShow().getId())
                        .name(showRecommendation.getShow().getName())
                        .genre(showRecommendation.getShow().getGenre())
                        .startDate(showRecommendation.getShow().getStartDate())
                        .endDate(showRecommendation.getShow().getEndDate())
                        .poster(showRecommendation.getShow().getPoster())
                        .build()
                ).toList();
    }
}
