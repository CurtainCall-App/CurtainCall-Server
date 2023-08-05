package org.cmc.curtaincall.web.service.show;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.repository.ShowRepository;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.cmc.curtaincall.web.service.show.request.ShowListRequest;
import org.cmc.curtaincall.web.service.show.response.ShowDetailResponse;
import org.cmc.curtaincall.web.service.show.response.ShowResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShowService {

    private final ShowRepository showRepository;

    public Slice<ShowResponse> getList(ShowListRequest request, Pageable pageable) {
        return showRepository.findSliceWithFacilityByGenreAndUseYnIsTrue(pageable, request.getGenre())
                .map(show -> ShowResponse.builder()
                        .id(show.getId())
                        .name(show.getName())
                        .startDate(show.getStartDate())
                        .endDate(show.getEndDate())
                        .facilityName(show.getFacility().getName())
                        .poster(show.getPoster())
                        .genre(show.getGenre())
                        .showTimes(show.getShowTimes())
                        .build()
                );
    }

    public ShowDetailResponse getDetail(String id) {
        Show show = getShowById(id);
        return ShowDetailResponse.builder()
                .id(show.getId())
                .name(show.getName())
                .startDate(show.getStartDate())
                .endDate(show.getEndDate())
                .facilityId(show.getFacility().getId())
                .facilityName(show.getFacility().getName())
                .crew(show.getCrew())
                .cast(show.getCast())
                .runtime(show.getRuntime())
                .age(show.getAge())
                .enterprise(show.getEnterprise())
                .ticketPrice(show.getTicketPrice())
                .poster(show.getPoster())
                .story(show.getStory())
                .genre(show.getGenre())
                .introductionImages(show.getIntroductionImages())
                .showTimes(show.getShowTimes())
                .reviewCount(show.getReviewCount())
                .reviewGradeSum(show.getReviewGradeSum())
                .build();
    }

    private Show getShowById(String id) {
        return showRepository.findById(id)
                .filter(Show::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Show id=" + id));
    }
}
