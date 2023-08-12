package org.cmc.curtaincall.web.controller;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.lostitem.request.LostItemQueryParam;
import org.cmc.curtaincall.web.exception.EntityAccessDeniedException;
import org.cmc.curtaincall.web.security.annotation.LoginMemberId;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.image.ImageService;
import org.cmc.curtaincall.web.service.lostitem.LostItemService;
import org.cmc.curtaincall.web.service.lostitem.request.LostItemCreate;
import org.cmc.curtaincall.web.service.lostitem.response.LostItemDetailResponse;
import org.cmc.curtaincall.web.service.lostitem.response.LostItemResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/lostitems")
    public Slice<LostItemResponse> search(
            Pageable pageable, @ModelAttribute LostItemQueryParam queryParam) {
        return lostItemService.search(pageable, queryParam);
    }

    @GetMapping("/lostitems/{lostItemId}")
    public LostItemDetailResponse getDetail(@PathVariable Long lostItemId) {
        return lostItemService.getDetail(lostItemId);
    }
}
