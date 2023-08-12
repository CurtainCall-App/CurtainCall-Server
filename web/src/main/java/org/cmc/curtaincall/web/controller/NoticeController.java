package org.cmc.curtaincall.web.controller;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.web.service.notice.NoticeService;
import org.cmc.curtaincall.web.service.notice.response.NoticeDetailResponse;
import org.cmc.curtaincall.web.service.notice.response.NoticeResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/notices")
    public Slice<NoticeResponse> getNoticeList(Pageable pageable) {
        return noticeService.getList(pageable);
    }

    @GetMapping("/notices/{noticeId}")
    public NoticeDetailResponse getNoticeDetail(@PathVariable Long noticeId) {
        return noticeService.getDetail(noticeId);
    }
}
