package org.cmc.curtaincall.web.show;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.show.FavoriteShow;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.ShowHelper;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.domain.show.repository.FavoriteShowRepository;
import org.cmc.curtaincall.domain.show.repository.ShowRepository;
import org.cmc.curtaincall.domain.show.validation.ShowFavoriteMemberValidator;
import org.cmc.curtaincall.web.show.response.FavoriteShowResponse;
import org.cmc.curtaincall.web.show.response.ShowFavoriteResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FavoriteShowService {

    private final FavoriteShowRepository favoriteShowRepository;

    private final ShowRepository showRepository;

    private final ShowFavoriteMemberValidator showFavoriteMemberValidator;

    @Transactional
    public void favorite(final MemberId memberId, final ShowId showId) {
        showFavoriteMemberValidator.validate(memberId);
        final Show show = ShowHelper.get(showId, showRepository);
        if (favoriteShowRepository.existsByMemberIdAndShow(memberId, show)) {
            return;
        }
        favoriteShowRepository.save(new FavoriteShow(show, memberId));
    }

    @Transactional
    public void cancelFavorite(final MemberId memberId, final ShowId showId) {
        final Show show = showRepository.getReferenceById(showId);
        favoriteShowRepository.findByMemberIdAndShow(memberId, show)
                .ifPresent(favoriteShowRepository::delete);
    }

    public List<ShowFavoriteResponse> areFavorite(Long memberId, List<String> showIds) {
        List<Show> shows = showIds.stream()
                .map(ShowId::new)
                .map(showRepository::getReferenceById)
                .toList();
        List<FavoriteShow> favoriteShows = favoriteShowRepository.findAllByMemberIdAndShowIn(new MemberId(memberId), shows);
        Set<String> favoriteShowIds = favoriteShows.stream()
                .map(favoriteShow -> favoriteShow.getShow().getId().getId())
                .collect(Collectors.toSet());
        return showIds.stream()
                .map(showId -> new ShowFavoriteResponse(new ShowId(showId), favoriteShowIds.contains(showId)))
                .toList();
    }

    public Slice<FavoriteShowResponse> getFavoriteShowList(Pageable pageable, Long memberId) {
        Slice<FavoriteShow> favoriteShows = favoriteShowRepository.findSliceWithShowByMemberId(pageable, new MemberId(memberId));
        List<FavoriteShowResponse> shows = favoriteShows.stream()
                .map(FavoriteShow::getShow)
                .filter(Show::getUseYn)
                .map(show -> FavoriteShowResponse.builder()
                        .id(show.getId())
                        .name(show.getName())
                        .startDate(show.getStartDate())
                        .endDate(show.getEndDate())
                        .facilityName(show.getFacility().getName())
                        .poster(show.getPoster())
                        .genre(show.getGenre())
                        .showTimes(new ArrayList<>(show.getShowTimes()))
                        .runtime(show.getRuntime())
                        .build()
                ).toList();
        return new SliceImpl<>(shows, pageable, favoriteShows.hasNext());
    }
}
