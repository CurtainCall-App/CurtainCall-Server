package org.cmc.curtaincall.web.service.show;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.show.repository.BoxOfficeRepository;
import org.cmc.curtaincall.web.service.show.request.BoxOfficeRequest;
import org.cmc.curtaincall.web.service.show.response.BoxOfficeResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoxOfficeService {

    private final BoxOfficeRepository boxOfficeRepository;

    public List<BoxOfficeResponse> getBoxOffice(BoxOfficeRequest request) {
        return boxOfficeRepository.findAllWithShowByBaseDateAndTypeAndGenreOrderByRank(
                        request.baseDate(), request.type(), request.genre()
                ).stream()
                .map(boxOffice -> BoxOfficeResponse.builder()
                        .id(boxOffice.getShow().getId())
                        .name(boxOffice.getShow().getName())
                        .startDate(boxOffice.getShow().getStartDate())
                        .endDate(boxOffice.getShow().getEndDate())
                        .poster(boxOffice.getShow().getPoster())
                        .genre(boxOffice.getShow().getGenre())
                        .reviewGradeSum(boxOffice.getShow().getReviewGradeSum())
                        .reviewCount(boxOffice.getShow().getReviewCount())
                        .rank(boxOffice.getRank())
                        .build())
                .toList();
    }
}
