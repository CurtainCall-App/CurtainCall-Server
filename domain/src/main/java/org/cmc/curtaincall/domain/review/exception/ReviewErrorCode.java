package org.cmc.curtaincall.domain.review.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.ErrorCodeType;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCodeType {
    NOT_FOUND("REVIEW-001", "리뷰가 존재하지 않습니다.", 404),
    ACCESS_DENIED("REVIEW-002", "작성자에게만 허용된 기능입니다.", 403),
    INVALID_GRADE("REVIEW-003", "리뷰 별점은 0~5 의 값이어야 합니다.", 400),
    /**
     * ShowReviewStats 은 Show 가 생성되면서 같이 생성되어야 하므로 이것이 존재하지 않으면
     * INTERNAL_SERVER_ERROR
     */
    STATS_NOT_FOUND("REVIEW-004", "리뷰통계가 존재하지 않습니다.", 500),
    ALREADY_REVIEWED("REVIEW-005", "해당 공연에 이미 리뷰가 등록되었습니다.", 409),
    ;

    private final String code;

    private final String message;

    private final int status;

}
