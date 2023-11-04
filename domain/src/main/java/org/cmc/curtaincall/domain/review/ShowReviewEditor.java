package org.cmc.curtaincall.domain.review;

import lombok.Builder;

@Builder
public record ShowReviewEditor(Integer grade, String content) {
}
