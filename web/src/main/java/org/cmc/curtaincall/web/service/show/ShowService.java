package org.cmc.curtaincall.web.service.show;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.review.ShowReviewStats;
import org.cmc.curtaincall.domain.review.repository.ShowReviewStatsRepository;
import org.cmc.curtaincall.domain.show.Facility;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowId;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShowService {

    private final ShowRepository showRepository;

    private final FacilityRepository facilityRepository;

    private final ShowDateTimeRepository showDateTimeRepository;

    private final ShowReviewStatsRepository showReviewStatsRepository;

    public Slice<ShowResponse> getList(ShowListRequest request, Pageable pageable) {
        return showRepository.findSliceWithFacilityByGenreAndStateAndUseYnIsTrue(
                pageable, request.getGenre(), request.getState()
        ).map(ShowResponse::of);
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
        final Slice<Show> shows = Optional.ofNullable(genre)
                .map(g -> showRepository.findSliceWithByFacilityAndGenreAndUseYnIsTrue(pageable, facility, genre))
                .orElseGet(() -> showRepository.findSliceWithByFacilityAndUseYnIsTrue(pageable, facility));
        final Map<ShowId, ShowReviewStats> showIdToReviewStats = showReviewStatsRepository.findAllById(
                        shows.stream().map(Show::getId).map(ShowId::new).toList())
                .stream()
                .collect(Collectors.toUnmodifiableMap(ShowReviewStats::getId, Function.identity()));
        return shows
                .map(show -> ShowResponse.builder()
                        .id(show.getId())
                        .name(show.getName())
                        .startDate(show.getStartDate())
                        .endDate(show.getEndDate())
                        .facilityName(facility.getName())
                        .poster(show.getPoster())
                        .genre(show.getGenre())
                        .showTimes(new ArrayList<>(show.getShowTimes()))
                        .reviewCount(showIdToReviewStats.get(new ShowId(show.getId())).getReviewCount())
                        .reviewGradeSum(showIdToReviewStats.get(new ShowId(show.getId())).getReviewGradeSum())
                        .runtime(show.getRuntime())
                        .build());
    }

    public ShowDetailResponse getDetail(String id) {
        Show show = getShowById(id);
        return ShowDetailResponse.of(show);
    }

    public List<ShowDateTimeResponse> getLiveTalkShowTimeList(LocalDateTime baseDateTime) {
        return Stream.of(
                        showDateTimeRepository.findAllByShowAtLessThanEqualAndShowAtGreaterThan(
                                baseDateTime.plusHours(2L), baseDateTime),
                        showDateTimeRepository.findAllByShowAtLessThanEqualAndShowEndAtGreaterThanEqual(
                                baseDateTime, baseDateTime),
                        showDateTimeRepository.findAllByShowEndAtLessThanEqualAndShowEndAtGreaterThan(
                                baseDateTime, baseDateTime.minusHours(2)
                        )
                )
                .flatMap(List::stream)
                .distinct()
                .map(ShowDateTimeResponse::of)
                .toList();
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
