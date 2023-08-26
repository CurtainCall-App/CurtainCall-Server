package org.cmc.curtaincall.web.service.show;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.show.repository.BoxOfficeRepository;
import org.cmc.curtaincall.web.service.show.request.BoxOfficeRequest;
import org.cmc.curtaincall.web.service.show.response.BoxOfficeResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoxOfficeService {

    private final BoxOfficeRepository boxOfficeRepository;

    public List<BoxOfficeResponse> getBoxOffice(BoxOfficeRequest request) {
        return Optional.of(boxOfficeRepository.findAllWithShowByBaseDateAndTypeAndGenreOrderByRank(
                        request.baseDate(), request.type(), request.genre()
                ))
                .filter(list -> !list.isEmpty())
                .orElseGet(() ->
                        Optional.ofNullable(boxOfficeRepository.findTopBaseDate(
                                        request.baseDate(), request.type(), request.genre()))
                                .map(topBaseDate -> boxOfficeRepository
                                        .findAllWithShowByBaseDateAndTypeAndGenreOrderByRank(
                                                topBaseDate, request.type(), request.genre()))
                                .orElse(Collections.emptyList())
                )
                .stream()
                .map(BoxOfficeResponse::of)
                .toList();
    }

}
