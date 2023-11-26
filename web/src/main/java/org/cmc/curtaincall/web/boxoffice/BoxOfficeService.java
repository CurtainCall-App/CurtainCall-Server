package org.cmc.curtaincall.web.boxoffice;

import org.cmc.curtaincall.web.boxoffice.dto.BoxOfficeRequest;
import org.cmc.curtaincall.web.boxoffice.dto.BoxOfficeResponse;

import java.util.List;

public interface BoxOfficeService {

    List<BoxOfficeResponse> getList(BoxOfficeRequest request);
}
