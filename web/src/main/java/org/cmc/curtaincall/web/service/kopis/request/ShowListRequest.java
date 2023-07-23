package org.cmc.curtaincall.web.service.kopis.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.cmc.curtaincall.web.service.kopis.ShowGenre;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class ShowListRequest {

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull
    private ShowGenre genre;

    private String name;
}
