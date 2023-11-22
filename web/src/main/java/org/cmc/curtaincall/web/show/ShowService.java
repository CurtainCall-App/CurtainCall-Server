package org.cmc.curtaincall.web.show;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.show.Facility;
import org.cmc.curtaincall.domain.show.FacilityId;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.domain.show.exception.FacilityNotFoundException;
import org.cmc.curtaincall.domain.show.exception.ShowNotFoundException;
import org.cmc.curtaincall.domain.show.repository.FacilityRepository;
import org.cmc.curtaincall.domain.show.repository.ShowDateTimeRepository;
import org.cmc.curtaincall.domain.show.repository.ShowRepository;
import org.cmc.curtaincall.web.show.request.ShowListRequest;
import org.cmc.curtaincall.web.show.response.ShowDateTimeResponse;
import org.cmc.curtaincall.web.show.response.ShowDetailResponse;
import org.cmc.curtaincall.web.show.response.ShowResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShowService {

    private final ShowRepository showRepository;

    private final FacilityRepository facilityRepository;

    private final ShowDateTimeRepository showDateTimeRepository;

    public List<ShowResponse> getList(final ShowListRequest request, final Pageable pageable) {
        return showRepository.findAllWithFacilityByGenreAndStateAndUseYnIsTrue(
                pageable, request.getGenre(), request.getState()
        ).stream().map(ShowResponse::of).toList();
    }

    public List<ShowResponse> search(final Pageable pageable, final String keyword) {
        return showRepository.findAllWithByNameStartsWithAndUseYnIsTrue(pageable, keyword)
                .stream().map(ShowResponse::of).toList();
    }

    public List<ShowResponse> getListToOpen(final Pageable pageable, final LocalDate startDate) {
        return showRepository.findAllWithByStartDateGreaterThanEqualAndUseYnIsTrue(pageable, startDate)
                .stream().map(ShowResponse::of).toList();
    }

    public List<ShowResponse> getListToEnd(
            final Pageable pageable, final LocalDate endDate, @Nullable final ShowGenre genre
    ) {
        return Optional.ofNullable(genre)
                .map(g -> showRepository.findAllWithByGenreAndEndDateGreaterThanEqualAndUseYnIsTrue(
                        pageable, g, endDate
                ))
                .orElseGet(() -> showRepository.findAllWithByEndDateGreaterThanEqualAndUseYnIsTrue(
                        pageable, endDate
                ))
                .stream().map(ShowResponse::of).toList();
    }

    public List<ShowResponse> getListOfFacility(
            final Pageable pageable, final FacilityId facilityId, @Nullable final ShowGenre genre) {
        Facility facility = getFacilityById(facilityId);
        final List<Show> shows = Optional.ofNullable(genre)
                .map(g -> showRepository.findAllWithByFacilityAndGenreAndUseYnIsTrue(pageable, facility, genre))
                .orElseGet(() -> showRepository.findAllWithByFacilityAndUseYnIsTrue(pageable, facility));
        return shows.stream()
                .map(show -> ShowResponse.builder()
                        .id(show.getId())
                        .name(show.getName())
                        .startDate(show.getStartDate())
                        .endDate(show.getEndDate())
                        .facilityName(facility.getName())
                        .poster(show.getPoster())
                        .genre(show.getGenre())
                        .showTimes(new ArrayList<>(show.getShowTimes()))
                        .runtime(show.getRuntime())
                        .build()
                ).toList();
    }

    public ShowDetailResponse getDetail(final ShowId id) {
        final Show show = getShowById(id);
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

    private Show getShowById(final ShowId id) {
        return showRepository.findById(id)
                .filter(Show::getUseYn)
                .orElseThrow(() -> new ShowNotFoundException(id));
    }

    private Facility getFacilityById(final FacilityId id) {
        return facilityRepository.findById(id)
                .filter(Facility::getUseYn)
                .orElseThrow(() -> new FacilityNotFoundException(id));
    }
}
