package org.cmc.curtaincall.web.lostitem;

import org.cmc.curtaincall.domain.lostitem.LostItemType;
import org.cmc.curtaincall.domain.lostitem.dao.LostItemDao;
import org.cmc.curtaincall.domain.lostitem.response.LostItemDetailResponse;
import org.cmc.curtaincall.domain.lostitem.response.LostItemMyResponse;
import org.cmc.curtaincall.domain.lostitem.response.LostItemResponse;
import org.cmc.curtaincall.domain.show.FacilityId;
import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.cmc.curtaincall.web.common.RestDocsAttribute.type;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LostItemQueryController.class)
class LostItemQueryControllerTest extends AbstractWebTest {

    @MockBean
    private LostItemDao lostItemDao;

    @Test
    void getLostItemList_Docs() throws Exception {
        // given
        LostItemResponse lostItemResponse = LostItemResponse.builder()
                .id(10L)
                .facilityId(new FacilityId("FC001298"))
                .facilityName("시온아트홀 (구. JK아트홀, 샘아트홀)")
                .type(LostItemType.ELECTRONIC_EQUIPMENT)
                .title("아이패드 핑크")
                .foundDate(LocalDate.of(2023, 3, 4))
                .foundTime(LocalTime.of(11, 23))
                .imageUrl("image-url")
                .createdAt(LocalDateTime.of(2023, 8, 31, 10, 50))
                .build();
        given(lostItemDao.search(any(), any())).willReturn(List.of(lostItemResponse));

        // expected
        mockMvc.perform(get("/lostItems")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "20")
                        .param("facilityId", "FC001298")
                        .param("type", LostItemType.ELECTRONIC_EQUIPMENT.name())
                        .param("foundDate", LocalDate.of(2023, 3, 4).toString())
                        .param("title", "아이패드")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("lostitem-search",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("페이지 사이즈").optional(),
                                parameterWithName("facilityId").description("공연시설 ID").optional(),
                                parameterWithName("type").description("분류")
                                        .attributes(type(LostItemType.class))
                                        .optional(),
                                parameterWithName("foundDate").description("습득일자").optional(),
                                parameterWithName("title").description("제목").optional()
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("id").description("공연 아이디"),
                                fieldWithPath("facilityId").description("공연시설 ID"),
                                fieldWithPath("facilityName").description("공연시설 이름"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("foundDate").description("습득일자"),
                                fieldWithPath("foundTime").description("습득시간").optional(),
                                fieldWithPath("type").description("분실물 타입")
                                        .type(LostItemType.class.getSimpleName()),
                                fieldWithPath("imageUrl").description("이미지"),
                                fieldWithPath("createdAt").description("생성일시")
                        )
                ));
    }

    @Test
    void getMyList_Docs() throws Exception {
        // given
        var responseList = List.of(
                LostItemMyResponse.builder()
                        .id(10L)
                        .facilityId(new FacilityId("FC001298"))
                        .facilityName("시온아트홀 (구. JK아트홀, 샘아트홀)")
                        .type(LostItemType.ELECTRONIC_EQUIPMENT)
                        .title("아이패드 핑크")
                        .foundDate(LocalDate.of(2023, 3, 4))
                        .foundTime(LocalTime.of(11, 23))
                        .imageUrl("image-url")
                        .createdAt(LocalDateTime.of(2023, 8, 31, 10, 50))
                        .build()
        );
        given(lostItemDao.getMyList(any(), any()))
                .willReturn(responseList);

        // expected
        mockMvc.perform(get("/member/lostItems")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "20")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("lostitem-get-my-list",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 사이즈").optional()
                        ),
                        responseFields(
                                beneathPath("content[]").withSubsectionId("content"),
                                fieldWithPath("id").description("공연 아이디"),
                                fieldWithPath("facilityId").description("공연시설 ID"),
                                fieldWithPath("facilityName").description("공연시설 이름"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("foundDate").description("습득일자"),
                                fieldWithPath("foundTime").description("습득시간").optional(),
                                fieldWithPath("type").description("분실물 타입")
                                        .type(LostItemType.class.getSimpleName()),
                                fieldWithPath("imageUrl").description("이미지"),
                                fieldWithPath("createdAt").description("생성일시")
                        )
                ));
    }

    @Test
    void getDetail_Docs() throws Exception {
        // given
        LostItemDetailResponse lostItemDetailResponse = LostItemDetailResponse.builder()
                .id(10L)
                .facilityId(new FacilityId("FC001298"))
                .facilityName("시온아트홀 (구. JK아트홀, 샘아트홀)")
                .facilityPhone("01-234-5678")
                .title("아이패드 핑크")
                .type(LostItemType.ELECTRONIC_EQUIPMENT)
                .foundPlaceDetail("2열")
                .foundDate(LocalDate.of(2023, 3, 4))
                .foundTime(LocalTime.of(11, 23))
                .particulars("기스있음")
                .imageId(12L)
                .imageUrl("image-url")
                .createdAt(LocalDateTime.of(2023, 8, 31, 10, 50))
                .build();
        given(lostItemDao.getDetail(any())).willReturn(lostItemDetailResponse);

        // expected
        mockMvc.perform(get("/lostItems/{lostItemId}", "10")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("lostitem-get-detail",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        pathParameters(
                                parameterWithName("lostItemId").description("분실물 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("공연 ID"),
                                fieldWithPath("id").description("공연 아이디"),
                                fieldWithPath("facilityId").description("공연시설 ID"),
                                fieldWithPath("facilityName").description("공연시설 이름"),
                                fieldWithPath("facilityPhone").description("공연시설 전화번호"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("type").type(LostItemType.class.getSimpleName()).description("분류"),
                                fieldWithPath("foundPlaceDetail").description("세부장소"),
                                fieldWithPath("foundDate").description("습득일자"),
                                fieldWithPath("foundTime").description("습득시간").optional(),
                                fieldWithPath("particulars").description("특이사항"),
                                fieldWithPath("imageId").description("이미지 ID"),
                                fieldWithPath("imageUrl").description("이미지"),
                                fieldWithPath("createdAt").description("생성일시")
                        )
                ));
    }
}