package org.cmc.curtaincall.web.lostitem;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.lostitem.LostItemId;
import org.cmc.curtaincall.domain.lostitem.validation.LostItemCreatorValidator;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.web.common.response.IdResult;
import org.cmc.curtaincall.web.exception.EntityAccessDeniedException;
import org.cmc.curtaincall.web.lostitem.request.LostItemCreate;
import org.cmc.curtaincall.web.lostitem.request.LostItemEdit;
import org.cmc.curtaincall.web.security.config.LoginMemberId;
import org.cmc.curtaincall.web.service.image.ImageService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LostItemController {

    private final LostItemService lostItemService;

    private final ImageService imageService;

    private final LostItemCreatorValidator lostItemCreatorValidator;

    @PostMapping("/lostItems")
    public IdResult<LostItemId> createLostItem(
            @RequestBody @Validated LostItemCreate lostItemCreate, @LoginMemberId MemberId memberId
    ) {
        if (!imageService.isOwnedByMember(memberId.getId(), lostItemCreate.getImageId())) {
            throw new EntityAccessDeniedException(
                    "Member ID=" + memberId + ", Image ID=" + lostItemCreate.getImageId());
        }
        return new IdResult<>(lostItemService.create(lostItemCreate));
    }

    @DeleteMapping("/lostItems/{lostItemId}")
    public void deleteLostItem(@PathVariable LostItemId lostItemId, @LoginMemberId MemberId memberId) {
        lostItemCreatorValidator.validate(lostItemId, new CreatorId(memberId));
        lostItemService.delete(lostItemId);
    }

    @PatchMapping("/lostItems/{lostItemId}")
    public void editLostItem(
            @PathVariable LostItemId lostItemId, @LoginMemberId MemberId memberId,
            @RequestBody @Validated LostItemEdit lostItemEdit) {
        lostItemCreatorValidator.validate(lostItemId, new CreatorId(memberId));
        if (!imageService.isOwnedByMember(memberId.getId(), lostItemEdit.getImageId())) {
            throw new EntityAccessDeniedException(
                    "Member ID=" + memberId + ", Image ID=" + lostItemEdit.getImageId());
        }
        lostItemService.edit(lostItemId, lostItemEdit);
    }
}
