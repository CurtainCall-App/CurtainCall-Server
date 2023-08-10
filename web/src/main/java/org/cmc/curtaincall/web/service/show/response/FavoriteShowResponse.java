package org.cmc.curtaincall.web.service.show.response;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FavoriteShowResponse {

    private String id;

    private String name;

    private String poster;

    private String story;

    private Integer reviewCount;

    private Long reviewGradeSum;
}
