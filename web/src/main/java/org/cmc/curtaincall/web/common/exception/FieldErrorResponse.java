package org.cmc.curtaincall.web.common.exception;

public record FieldErrorResponse(
        String field,
        Object value,
        String reason
) {
}
