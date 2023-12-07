package org.cmc.curtaincall.web.notice;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.notice.Notice;
import org.cmc.curtaincall.domain.notice.NoticeId;
import org.cmc.curtaincall.domain.notice.exception.NoticeNotFoundException;
import org.cmc.curtaincall.domain.notice.repository.NoticeRepository;
import org.cmc.curtaincall.web.notice.response.NoticeDetailResponse;
import org.cmc.curtaincall.web.notice.response.NoticeResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public List<NoticeResponse> getList(Pageable pageable) {
        return noticeRepository.findAllByUseYnIsTrue(pageable).stream()
                .map(notice -> NoticeResponse.builder()
                        .id(notice.getId())
                        .title(notice.getTitle())
                        .createdAt(notice.getCreatedAt())
                        .build()
                ).toList();
    }

    public NoticeDetailResponse getDetail(NoticeId id) {
        Notice notice = getNoticeById(id);
        return NoticeDetailResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .build();
    }

    private Notice getNoticeById(NoticeId id) {
        return noticeRepository.findById(id.getId())
                .filter(Notice::getUseYn)
                .orElseThrow(() -> new NoticeNotFoundException(id));
    }
}
