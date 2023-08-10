package org.cmc.curtaincall.web.controller;

import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.web.security.annotation.LoginMemberId;
import org.cmc.curtaincall.web.service.show.FavoriteShowService;
import org.cmc.curtaincall.web.service.show.response.ShowFavoriteResponse;
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
    public void favoriteShow(@PathVariable String showId, @LoginMemberId Long memberId) {
        favoriteShowService.favorite(memberId, showId);
    }

    @DeleteMapping("/shows/{showId}/favorite")
    public void cancelFavorite(@PathVariable String showId, @LoginMemberId Long memberId) {
        favoriteShowService.cancelFavorite(memberId, showId);
    }

    @GetMapping("/member/favorite")
    public Slice<ShowFavoriteResponse> getFavorite(
            @RequestParam @Validated @Size(max = 100) List<String> showIds, @LoginMemberId Long memberId) {
        List<ShowFavoriteResponse> showFavoriteResponses = favoriteShowService.areFavorite(memberId, showIds);
        return new SliceImpl<>(showFavoriteResponses);
    }
}
