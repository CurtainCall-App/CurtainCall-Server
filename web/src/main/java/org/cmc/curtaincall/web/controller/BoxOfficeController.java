package org.cmc.curtaincall.web.controller;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.web.service.show.BoxOfficeService;
import org.cmc.curtaincall.web.service.show.request.BoxOfficeRequest;
import org.cmc.curtaincall.web.service.show.response.BoxOfficeResponse;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoxOfficeController {

    private final BoxOfficeService boxOfficeService;

    @GetMapping("/box-office")
    public Slice<BoxOfficeResponse> getBoxOffice(@ModelAttribute @Validated BoxOfficeRequest request) {
        List<BoxOfficeResponse> boxOffice = boxOfficeService.getBoxOffice(request);
        return new SliceImpl<>(boxOffice);
    }
}
