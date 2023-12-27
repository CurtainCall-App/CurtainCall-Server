package org.cmc.curtaincall.web.show;

import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.web.common.response.ListResult;
import org.cmc.curtaincall.web.security.config.LoginMemberId;
import org.cmc.curtaincall.web.show.response.FavoriteShowResponse;
import org.cmc.curtaincall.web.show.response.ShowFavoriteResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FavoriteShowController {

    private final FavoriteShowService favoriteShowService;

    @PutMapping("/shows/{showId}/favorite")
    public void favoriteShow(@PathVariable ShowId showId, @LoginMemberId MemberId memberId) {
        favoriteShowService.favorite(memberId, showId);
    }

    @DeleteMapping("/shows/{showId}/favorite")
    public void cancelFavorite(@PathVariable ShowId showId, @LoginMemberId MemberId memberId) {
        favoriteShowService.cancelFavorite(memberId, showId);
    }

    @GetMapping("/member/favorite")
    public ListResult<ShowFavoriteResponse> getFavorite(
            @RequestParam @Size(max = 100) final List<ShowId> showIds,
            @LoginMemberId final MemberId memberId
    ) {
        final List<ShowFavoriteResponse> showFavoriteResponses = favoriteShowService.areFavorite(memberId, showIds);
        return new ListResult<>(showFavoriteResponses);
    }

    @GetMapping("/members/{memberId}/favorite")
    public Slice<FavoriteShowResponse> getFavoriteShowList(
            @PathVariable MemberId memberId, Pageable pageable) {
        return favoriteShowService.getFavoriteShowList(pageable, memberId.getId());
    }
}
