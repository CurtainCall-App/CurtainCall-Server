package org.cmc.curtaincall.web.notice;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.notice.NoticeId;
import org.cmc.curtaincall.web.common.response.ListResult;
import org.cmc.curtaincall.web.notice.response.NoticeDetailResponse;
import org.cmc.curtaincall.web.notice.response.NoticeResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/notices")
    public ListResult<NoticeResponse> getNoticeList(
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        List<NoticeResponse> notices = noticeService.getList(pageable);
        return new ListResult<>(notices);
    }

    @GetMapping("/notices/{noticeId}")
    public NoticeDetailResponse getNoticeDetail(@PathVariable Long noticeId) {
        return noticeService.getDetail(new NoticeId(noticeId));
    }
}
