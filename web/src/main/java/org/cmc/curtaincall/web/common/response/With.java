package org.cmc.curtaincall.web.common.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record With<T, U>(
        @JsonUnwrapped T t,
        @JsonUnwrapped U u
) {
}
