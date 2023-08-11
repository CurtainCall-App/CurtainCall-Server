package org.cmc.curtaincall.web.controller;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.web.exception.EntityAccessDeniedException;
import org.cmc.curtaincall.web.security.annotation.LoginMemberId;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.image.ImageService;
import org.cmc.curtaincall.web.service.lostitem.LostItemService;
import org.cmc.curtaincall.web.service.lostitem.request.LostItemCreate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LostItemController {

    private final LostItemService lostItemService;

    private final ImageService imageService;

    @PostMapping("/lostitems")
    public IdResult<Long> createLostItem(@RequestBody @Validated LostItemCreate lostItemCreate, @LoginMemberId Long memberId) {
        if (!imageService.isOwnedByMember(memberId, lostItemCreate.getImageId())) {
            throw new EntityAccessDeniedException(
                    "Member ID=" + memberId + ", Image ID=" + lostItemCreate.getImageId());
        }
        return lostItemService.create(lostItemCreate);
    }
}
