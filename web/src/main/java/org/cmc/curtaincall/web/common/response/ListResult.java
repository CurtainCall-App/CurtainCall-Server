package org.cmc.curtaincall.web.common.response;

import java.util.List;

public record ListResult<T>(
        List<T> content
) {
}
