package org.cmc.curtaincall.web.service.show;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.member.repository.MemberRepository;
import org.cmc.curtaincall.domain.show.FavoriteShow;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.repository.FavoriteShowRepository;
import org.cmc.curtaincall.domain.show.repository.ShowRepository;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.cmc.curtaincall.web.service.show.response.ShowFavoriteResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteShowService {

    private final FavoriteShowRepository favoriteShowRepository;

    private final MemberRepository memberRepository;

    private final ShowRepository showRepository;

    public void favorite(Long memberId, String showId) {
        Show show = getShowById(showId);
        Member member = memberRepository.getReferenceById(memberId);
        if (favoriteShowRepository.existsByMemberAndShow(member, show)) {
            return;
        }
        favoriteShowRepository.save(new FavoriteShow(show, member));
    }

    public void cancelFavorite(Long memberId, String showId) {
        Show show = getShowById(showId);
        Member member = memberRepository.getReferenceById(memberId);
        favoriteShowRepository.findByMemberAndShow(member, show)
                .ifPresent(favoriteShowRepository::delete);
    }

    public List<ShowFavoriteResponse> areFavorite(Long memberId, List<String> showIds) {
        Member member = memberRepository.getReferenceById(memberId);
        List<Show> shows = showIds.stream()
                .map(showRepository::getReferenceById)
                .toList();
        List<FavoriteShow> favoriteShows = favoriteShowRepository.findAllByMemberAndShowIn(member, shows);
        Set<String> favoriteShowIds = favoriteShows.stream()
                .map(favoriteShow -> favoriteShow.getShow().getId())
                .collect(Collectors.toSet());
        return showIds.stream()
                .map(showId -> new ShowFavoriteResponse(showId, favoriteShowIds.contains(showId)))
                .toList();
    }

    private Show getShowById(String id) {
        return showRepository.findById(id)
                .filter(Show::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Show id=" + id));
    }
}
