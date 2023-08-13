package org.cmc.curtaincall.web.service.notice;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.notice.Notice;
import org.cmc.curtaincall.domain.notice.repository.NoticeRepository;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.cmc.curtaincall.web.service.notice.response.NoticeDetailResponse;
import org.cmc.curtaincall.web.service.notice.response.NoticeResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public Slice<NoticeResponse> getList(Pageable pageable) {
        return noticeRepository.findSliceByUseYnIsTrueOrderByCreatedAtDesc(pageable)
                .map(notice -> NoticeResponse.builder()
                        .id(notice.getId())
                        .title(notice.getTitle())
                        .createdAt(notice.getCreatedAt())
                        .build()
                );
    }

    public NoticeDetailResponse getDetail(Long id) {
        Notice notice = getNoticeById(id);
        return NoticeDetailResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .build();
    }

    private Notice getNoticeById(Long id) {
        return noticeRepository.findById(id)
                .filter(Notice::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Notice id=" + id));
    }
}
