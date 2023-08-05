package org.cmc.curtaincall.web.service.kopis.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class ShowListRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull
    private ShowGenre genre;

    private String name;
}
