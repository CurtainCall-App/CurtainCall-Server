package org.cmc.curtaincall.web.service.notice.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeResponse {

    private Long id;

    private String title;

    private LocalDateTime createdAt;
}
