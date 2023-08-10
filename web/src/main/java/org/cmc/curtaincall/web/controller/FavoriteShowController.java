package org.cmc.curtaincall.web.controller;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.web.security.annotation.LoginMemberId;
import org.cmc.curtaincall.web.service.show.FavoriteShowService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
