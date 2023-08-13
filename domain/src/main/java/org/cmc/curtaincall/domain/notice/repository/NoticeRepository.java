package org.cmc.curtaincall.domain.notice.repository;

import org.cmc.curtaincall.domain.notice.Notice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Slice<Notice> findSliceByUseYnIsTrueOrderByCreatedAtDesc(Pageable pageable);
}
