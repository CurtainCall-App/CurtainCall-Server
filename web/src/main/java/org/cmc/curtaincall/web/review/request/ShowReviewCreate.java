package org.cmc.curtaincall.web.review.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.cmc.curtaincall.domain.show.ShowId;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class ShowReviewCreate {

    @NotNull
    private ShowId showId;

    @PositiveOrZero
    @Max(5L)
    @NotNull
    private Integer grade;

    @NotBlank
    @Size(max = 200)
    private String content;
}
