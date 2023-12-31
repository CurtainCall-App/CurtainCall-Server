package org.cmc.curtaincall.web.lostitem;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.AllowedSort;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.core.OrderParam;
import org.cmc.curtaincall.domain.core.SortParam;
import org.cmc.curtaincall.domain.lostitem.LostItemId;
import org.cmc.curtaincall.domain.lostitem.dao.LostItemDao;
import org.cmc.curtaincall.domain.lostitem.request.LostItemListQueryParam;
import org.cmc.curtaincall.domain.lostitem.request.LostItemSearchParam;
import org.cmc.curtaincall.domain.lostitem.response.LostItemDetailResponse;
import org.cmc.curtaincall.domain.lostitem.response.LostItemMyResponse;
import org.cmc.curtaincall.domain.lostitem.response.LostItemResponse;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.web.common.response.ListResult;
import org.cmc.curtaincall.web.security.config.LoginMemberId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LostItemQueryController {

    private final LostItemDao lostItemDao;

    @GetMapping("/lostItems")
    public ListResult<LostItemResponse> getList(
            @AllowedSort(@SortParam(@OrderParam(property = "createdAt", direction = Sort.Direction.DESC)))
            final Pageable pageable,
            @ModelAttribute @Validated final LostItemListQueryParam queryParam
    ) {
        return new ListResult<>(lostItemDao.getList(pageable, queryParam));
    }

    @GetMapping("/search-lostItems")
    public ListResult<LostItemResponse> search(
            @AllowedSort final Pageable pageable,
            @ModelAttribute @Validated final LostItemSearchParam searchParam
    ) {
        return new ListResult<>(lostItemDao.search(pageable, searchParam));
    }

    @GetMapping("/member/lostItems")
    public ListResult<LostItemMyResponse> getMyList(
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @LoginMemberId MemberId memberId
    ) {
        return new ListResult<>(lostItemDao.getMyList(pageable, new CreatorId(memberId)));
    }

    @GetMapping("/lostItems/{lostItemId}")
    public LostItemDetailResponse getDetail(@PathVariable LostItemId lostItemId) {
        return lostItemDao.getDetail(lostItemId);
    }
}
