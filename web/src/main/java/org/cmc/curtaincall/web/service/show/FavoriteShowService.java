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
import org.springframework.stereotype.Service;

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

    private Show getShowById(String id) {
        return showRepository.findById(id)
                .filter(Show::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Show id=" + id));
    }
}
