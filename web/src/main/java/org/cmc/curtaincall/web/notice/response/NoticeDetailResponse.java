package org.cmc.curtaincall.web.notice.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeDetailResponse {

    private Long id;

    private String title;

    private String content;

    private LocalDateTime createdAt;
}
