package org.cmc.curtaincall.batch.job.show;

import org.cmc.curtaincall.batch.service.kopis.response.ShowResponse;

public record ShowPresentResponse(
        ShowResponse showResponse,
        boolean present
) {
}
