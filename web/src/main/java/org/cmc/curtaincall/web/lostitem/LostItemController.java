package org.cmc.curtaincall.web.lostitem;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.lostitem.LostItemId;
import org.cmc.curtaincall.domain.lostitem.dao.LostItemDao;
import org.cmc.curtaincall.domain.lostitem.request.LostItemQueryParam;
import org.cmc.curtaincall.domain.lostitem.response.LostItemDetailResponse;
import org.cmc.curtaincall.domain.lostitem.response.LostItemMyResponse;
import org.cmc.curtaincall.domain.lostitem.response.LostItemResponse;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.web.common.response.IdResult;
import org.cmc.curtaincall.web.common.response.ListResult;
import org.cmc.curtaincall.web.exception.EntityAccessDeniedException;
import org.cmc.curtaincall.web.lostitem.request.LostItemCreate;
import org.cmc.curtaincall.web.lostitem.request.LostItemEdit;
import org.cmc.curtaincall.web.security.LoginMemberId;
import org.cmc.curtaincall.web.service.image.ImageService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LostItemController {

    private final LostItemService lostItemService;

    private final ImageService imageService;

    private final LostItemDao lostItemDao;

    @PostMapping("/lostItems")
    public IdResult<Long> createLostItem(
            @RequestBody @Validated LostItemCreate lostItemCreate, @LoginMemberId MemberId memberId
    ) {
        if (!imageService.isOwnedByMember(memberId.getId(), lostItemCreate.getImageId())) {
            throw new EntityAccessDeniedException(
                    "Member ID=" + memberId + ", Image ID=" + lostItemCreate.getImageId());
        }
        return lostItemService.create(lostItemCreate);
    }

    @GetMapping("/lostItems")
    public ListResult<LostItemResponse> search(
            Pageable pageable, @ModelAttribute LostItemQueryParam queryParam) {
        return new ListResult<>(lostItemDao.search(pageable, queryParam));
    }

    @GetMapping("/member/lostItems")
    public ListResult<LostItemMyResponse> getMyLostItemList(
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @LoginMemberId MemberId memberId
    ) {
        return new ListResult<>(lostItemDao.getMyList(pageable, new CreatorId(memberId)));
    }

    @GetMapping("/lostItems/{lostItemId}")
    public LostItemDetailResponse getDetail(@PathVariable Long lostItemId) {
        return lostItemDao.getDetail(new LostItemId(lostItemId));
    }

    @DeleteMapping("/lostItems/{lostItemId}")
    public void deleteLostItem(@PathVariable Long lostItemId, @LoginMemberId MemberId memberId) {
        if (!lostItemService.isOwnedByMember(lostItemId, memberId.getId())) {
            throw new EntityAccessDeniedException("lostItemId=" + lostItemId + "memberId=" + memberId);
        }
        lostItemService.delete(lostItemId);
    }

    @PatchMapping("/lostItems/{lostItemId}")
    public void editLostItem(
            @PathVariable Long lostItemId, @LoginMemberId MemberId memberId,
            @RequestBody @Validated LostItemEdit lostItemEdit) {
        if (!lostItemService.isOwnedByMember(lostItemId, memberId.getId())) {
            throw new EntityAccessDeniedException("lostItemId=" + lostItemId + "memberId=" + memberId);
        }
        if (!imageService.isOwnedByMember(memberId.getId(), lostItemEdit.getImageId())) {
            throw new EntityAccessDeniedException(
                    "Member ID=" + memberId + ", Image ID=" + lostItemEdit.getImageId());
        }
        lostItemService.edit(lostItemId, lostItemEdit);
    }
}
