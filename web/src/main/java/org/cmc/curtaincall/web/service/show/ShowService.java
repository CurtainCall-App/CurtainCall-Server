package org.cmc.curtaincall.web.service.show;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.repository.ShowRepository;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.cmc.curtaincall.web.service.show.request.ShowListRequest;
import org.cmc.curtaincall.web.service.show.response.ShowDetailResponse;
import org.cmc.curtaincall.web.service.show.response.ShowResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShowService {

    private final ShowRepository showRepository;

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

    public ShowDetailResponse getDetail(String id) {
        Show show = getShowById(id);
        return ShowDetailResponse.of(show);
    }

    private Show getShowById(String id) {
        return showRepository.findById(id)
                .filter(Show::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Show id=" + id));
    }
}
