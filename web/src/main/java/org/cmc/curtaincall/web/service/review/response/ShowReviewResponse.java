package org.cmc.curtaincall.web.service.review.response;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ShowReviewResponse {

    private Long id;

    private String showId;

    private Integer grade;

    private String content;

    private Long creatorId;

    private String creatorNickname;

    private String creatorImageUrl;
}
