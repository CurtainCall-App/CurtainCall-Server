package org.cmc.curtaincall.web.service.show;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.show.repository.ShowRepository;
import org.cmc.curtaincall.web.service.show.request.ShowListRequest;
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
}
