package org.cmc.curtaincall.web.boxoffice;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.web.boxoffice.dto.BoxOfficeRequest;
import org.cmc.curtaincall.web.boxoffice.dto.BoxOfficeResponse;
import org.cmc.curtaincall.web.common.response.ListResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoxOfficeController {

    private final BoxOfficeService boxOfficeService;

    @GetMapping("/box-office")
    public ListResult<BoxOfficeResponse> getList(@ModelAttribute @Validated BoxOfficeRequest request) {
        return new ListResult<>(boxOfficeService.getList(request));
    }
}
