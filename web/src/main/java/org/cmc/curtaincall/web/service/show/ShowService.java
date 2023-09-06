package org.cmc.curtaincall.web.service.show;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.show.Facility;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.repository.FacilityRepository;
import org.cmc.curtaincall.domain.show.repository.ShowDateTimeRepository;
import org.cmc.curtaincall.domain.show.repository.ShowRepository;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.cmc.curtaincall.web.service.show.request.ShowListRequest;
import org.cmc.curtaincall.web.service.show.response.ShowDateTimeResponse;
import org.cmc.curtaincall.web.service.show.response.ShowDetailResponse;
import org.cmc.curtaincall.web.service.show.response.ShowResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShowService {

    private final ShowRepository showRepository;

    private final FacilityRepository facilityRepository;

    private final ShowDateTimeRepository showDateTimeRepository;

    public Slice<ShowResponse> getList(ShowListRequest request, Pageable pageable) {
        return showRepository.findSliceWithFacilityByGenreAndUseYnIsTrue(pageable, request.getGenre())
                .map(ShowResponse::of);
    }

    public Slice<ShowResponse> search(Pageable pageable, String keyword) {
        return showRepository.findSliceWithByNameStartsWithAndUseYnIsTrue(pageable, keyword)
                .map(ShowResponse::of);
    }

    public Slice<ShowResponse> getListToOpen(Pageable pageable, LocalDate startDate) {
        return showRepository.findSliceWithByStartDateGreaterThanEqualAndUseYnIsTrue(pageable, startDate)
                .map(ShowResponse::of);
    }

    public Slice<ShowResponse> getListToEnd(Pageable pageable, LocalDate endDate, @Nullable ShowGenre genre) {
        return Optional.ofNullable(genre)
                .map(g -> showRepository.findSliceWithByGenreAndEndDateGreaterThanEqualAndUseYnIsTrue(
                        pageable, g, endDate
                ))
                .orElseGet(() -> showRepository.findSliceWithByEndDateGreaterThanEqualAndUseYnIsTrue(
                        pageable, endDate
                ))
                .map(ShowResponse::of);
    }

    public Slice<ShowResponse> getListOfFacility(Pageable pageable, String facilityId, @Nullable ShowGenre genre) {
        Facility facility = getFacilityById(facilityId);
        return Optional.ofNullable(genre)
                .map(g -> showRepository.findSliceWithByFacilityAndGenreAndUseYnIsTrue(pageable, facility, genre))
                .orElseGet(() -> showRepository.findSliceWithByFacilityAndUseYnIsTrue(pageable, facility))
                .map(show -> ShowResponse.builder()
                        .id(show.getId())
                        .name(show.getName())
                        .startDate(show.getStartDate())
                        .endDate(show.getEndDate())
                        .facilityName(facility.getName())
                        .poster(show.getPoster())
                        .genre(show.getGenre())
                        .showTimes(new ArrayList<>(show.getShowTimes()))
                        .reviewCount(show.getReviewCount())
                        .reviewGradeSum(show.getReviewGradeSum())
                        .runtime(show.getRuntime())
                        .build());
    }

    public ShowDetailResponse getDetail(String id) {
        Show show = getShowById(id);
        return ShowDetailResponse.of(show);
    }

    public Slice<ShowDateTimeResponse> getShowTimeList(
            Pageable pageable, LocalDateTime showAt, LocalDateTime showEndAt
    ) {
        return showDateTimeRepository.findSliceByShowAtAfterAndShowEndAtBefore(
                pageable, showAt, showEndAt
        ).map(ShowDateTimeResponse::of);
    }

    private Show getShowById(String id) {
        return showRepository.findById(id)
                .filter(Show::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Show id=" + id));
    }

    private Facility getFacilityById(String id) {
        return facilityRepository.findById(id)
                .filter(Facility::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Facility id=" + id));
    }
}
