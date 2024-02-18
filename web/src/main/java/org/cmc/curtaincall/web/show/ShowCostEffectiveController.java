package org.cmc.curtaincall.web.show;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.show.dao.ShowCostEffectiveDao;
import org.cmc.curtaincall.domain.show.request.ShowCostEffectiveListParam;
import org.cmc.curtaincall.domain.show.response.ShowCostEffectiveResponse;
import org.cmc.curtaincall.web.common.response.ListResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ShowCostEffectiveController {

    private final ShowCostEffectiveDao showCostEffectiveDao;

    @GetMapping("/cost-effective-shows")
    public ListResult<ShowCostEffectiveResponse> getList(@Validated final ShowCostEffectiveListParam param) {
        return new ListResult<>(showCostEffectiveDao.getList(param));
    }
}
