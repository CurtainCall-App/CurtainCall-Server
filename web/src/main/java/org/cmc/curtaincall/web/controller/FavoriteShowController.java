package org.cmc.curtaincall.web.controller;

import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.web.security.annotation.LoginMemberId;
import org.cmc.curtaincall.web.service.show.FavoriteShowService;
import org.cmc.curtaincall.web.service.show.response.FavoriteShowResponse;
import org.cmc.curtaincall.web.service.show.response.ShowFavoriteResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FavoriteShowController {

    private final FavoriteShowService favoriteShowService;

    @PutMapping("/shows/{showId}/favorite")
    public void favoriteShow(@PathVariable String showId, @LoginMemberId MemberId memberId) {
        favoriteShowService.favorite(memberId.getId(), showId);
    }

    @DeleteMapping("/shows/{showId}/favorite")
    public void cancelFavorite(@PathVariable String showId, @LoginMemberId MemberId memberId) {
        favoriteShowService.cancelFavorite(memberId.getId(), showId);
    }

    @GetMapping("/member/favorite")
    public Slice<ShowFavoriteResponse> getFavorite(
            @RequestParam @Validated @Size(max = 100) List<String> showIds, @LoginMemberId MemberId memberId) {
        List<ShowFavoriteResponse> showFavoriteResponses = favoriteShowService.areFavorite(memberId.getId(), showIds);
        return new SliceImpl<>(showFavoriteResponses);
    }

    @GetMapping("/members/{memberId}/favorite")
    public Slice<FavoriteShowResponse> getFavoriteShowList(
            @PathVariable Long memberId, Pageable pageable) {
        return favoriteShowService.getFavoriteShowList(pageable, memberId);
    }
}
