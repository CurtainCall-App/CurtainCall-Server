package org.cmc.curtaincall.web.service.kopis.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.cmc.curtaincall.web.service.kopis.ShowGenre;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class ShowBoxOfficeRequest {

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate baseDate;

    @NotNull
    private Type type;

    private ShowGenre genre;

    public enum Type {
        MONTH,
        WEEK,
        DAY,
    }
}
