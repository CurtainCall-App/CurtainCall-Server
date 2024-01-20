package org.cmc.curtaincall.web.recommend;

import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShowRecommendationController.class)
class ShowRecommendationControllerDocsTest extends AbstractWebTest {

    @MockBean
    private ShowRecommendationService showRecommendationService;

    @Test
    void getList_Docs() throws Exception {
        // given
        final var showRecommendationList = List.of(ShowRecommendationResponse.builder()
                .id(1L)
                .description("너무나도 재밌는 공연")
                .showId(new ShowId("PF220846"))
                .name("잘자요, 엄마 [청주]")
                .genre(ShowGenre.PLAY)
                .startDate(LocalDate.of(2023, 4, 28))
                .endDate(LocalDate.of(2023, 5, 12))
                .build()
        );

        given(showRecommendationService.getList()).willReturn(showRecommendationList);

        // expected
        mockMvc.perform(RestDocumentationRequestBuilders.get("/show-recommendations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("show-recommendation/get-list",
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("id").description("공연 추천 ID"),
                                fieldWithPath("description").description("추천 설명"),
                                fieldWithPath("showId").description("공연 ID"),
                                fieldWithPath("name").description("공연 이름"),
                                fieldWithPath("genre").description("공연 장르"),
                                fieldWithPath("startDate").description("공연 시작일"),
                                fieldWithPath("endDate").description("공연 종료일")
                        )
                ));
    }
}