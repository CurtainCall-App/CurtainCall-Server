package org.cmc.curtaincall.web.show;

import org.cmc.curtaincall.web.show.request.BoxOfficeRequest;
import org.cmc.curtaincall.web.show.response.BoxOfficeResponse;

import java.util.List;

public interface BoxOfficeService {

    List<BoxOfficeResponse> getList(BoxOfficeRequest request);
}
