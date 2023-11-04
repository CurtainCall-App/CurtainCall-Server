package org.cmc.curtaincall.web.review.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class ShowReviewEdit {

    @PositiveOrZero
    @Max(5L)
    @NotNull
    private Integer grade;

    @NotBlank
    @Size(max = 200)
    private String content;
}
