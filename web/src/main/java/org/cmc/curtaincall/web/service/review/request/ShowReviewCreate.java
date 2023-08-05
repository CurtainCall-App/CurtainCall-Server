package org.cmc.curtaincall.web.service.review.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShowReviewCreate {

    @PositiveOrZero
    @Max(10L)
    @NotNull
    private Integer grade;

    @NotBlank
    @Size(max = 200)
    private String content;
}
