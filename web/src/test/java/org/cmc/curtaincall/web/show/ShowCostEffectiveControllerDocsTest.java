package org.cmc.curtaincall.web.show;

import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.domain.show.dao.ShowCostEffectiveDao;
import org.cmc.curtaincall.domain.show.request.ShowCostEffectiveListParam;
import org.cmc.curtaincall.domain.show.response.ShowCostEffectiveResponse;
import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShowCostEffectiveController.class)
class ShowCostEffectiveControllerDocsTest extends AbstractWebTest {

    @MockBean
    private ShowCostEffectiveDao showCostEffectiveDao;

    @Test
    void getList_Docs() throws Exception {
        // given
        final var param = ShowCostEffectiveListParam.builder()
                .genre(ShowGenre.PLAY)
                .build();
        final var response = List.of(
                ShowCostEffectiveResponse.builder()
                        .id(new ShowId("PF220846"))
                        .name("잘자요, 엄마 [청주]")
                        .startDate(LocalDate.of(2023, 4, 28))
                        .endDate(LocalDate.of(2023, 5, 12))
                        .poster("http://www.kopis.or.kr/upload/pfmPoster/PF_PF220846_230704_164730.jpg")
                        .genre(ShowGenre.PLAY)
                        .minTicketPrice(20000)
                        .build()
        );

        given(showCostEffectiveDao.getList(param)).willReturn(response);

        // expected
        mockMvc.perform(RestDocumentationRequestBuilders.get("/cost-effective-shows")
                        .param("genre", ShowGenre.PLAY.name())
                )
                .andExpect(status().isOk())
                .andDo(document("show-cost-effective-list",
                        queryParameters(
                                parameterWithName("genre").description("공연 장르")
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("id").description("공연 ID"),
                                fieldWithPath("name").description("공연명"),
                                fieldWithPath("startDate").description("공연 시작일"),
                                fieldWithPath("endDate").description("공연 종료일"),
                                fieldWithPath("poster").description("공연 포스터 경로"),
                                fieldWithPath("genre").description("공연 장르명"),
                                fieldWithPath("minTicketPrice").description("최소 티켓 가격")
                        )
                ));
    }
}