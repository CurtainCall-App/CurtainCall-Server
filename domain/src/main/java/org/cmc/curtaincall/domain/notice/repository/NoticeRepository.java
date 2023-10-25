package org.cmc.curtaincall.domain.notice.repository;

import org.cmc.curtaincall.domain.notice.Notice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findAllByUseYnIsTrue(Pageable pageable);
}
