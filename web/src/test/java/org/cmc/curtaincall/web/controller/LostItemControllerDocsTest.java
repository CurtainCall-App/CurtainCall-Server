package org.cmc.curtaincall.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cmc.curtaincall.domain.lostitem.LostItemType;
import org.cmc.curtaincall.web.common.RestDocsConfig;
import org.cmc.curtaincall.web.service.account.AccountService;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.image.ImageService;
import org.cmc.curtaincall.web.service.lostitem.LostItemService;
import org.cmc.curtaincall.web.service.lostitem.request.LostItemCreate;
import org.cmc.curtaincall.web.service.lostitem.response.LostItemDetailResponse;
import org.cmc.curtaincall.web.service.lostitem.response.LostItemResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(RestDocsConfig.class)
@AutoConfigureRestDocs
@WebMvcTest(LostItemController.class)
class LostItemControllerDocsTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AccountService accountService;

    @MockBean
    LostItemService lostItemService;

    @MockBean
    ImageService imageService;

    @Test
    @WithMockUser
    void createLostItem_Docs() throws Exception {
        // given
        given(accountService.getMemberId(any())).willReturn(5L);

        LostItemCreate lostItemCreate = LostItemCreate.builder()
                .title("아이폰 핑크")
                .type(LostItemType.ELECTRONIC_EQUIPMENT)
                .facilityId("FC001298")
                .foundPlaceDetail("2열 8석")
                .foundDate(LocalDate.of(2023, 3, 4))
                .foundTime(LocalTime.of(11, 23))
                .particulars("기스 많음")
                .imageId(1L)
                .build();
        given(imageService.isOwnedByMember(any(), any())).willReturn(true);
        given(lostItemService.create(any())).willReturn(new IdResult<>(10L));

        // expected
        mockMvc.perform(post("/lostItems")
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lostItemCreate))
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("lostitem-create-lostitem",
                        requestFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("type").type(LostItemType.class.getSimpleName()).description("분류"),
                                fieldWithPath("facilityId").description("습득장소(공연장) ID"),
                                fieldWithPath("foundPlaceDetail").description("세부장수"),
                                fieldWithPath("foundDate").description("습득일자"),
                                fieldWithPath("foundTime").description("습득시간"),
                                fieldWithPath("particulars").description("특이사항"),
                                fieldWithPath("imageId").description("이미지 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("분실물 ID")
                        )
                ));
    }

    @Test
    @WithMockUser
    void getShows_Docs() throws Exception {
        // given
        LostItemResponse lostItemResponse = LostItemResponse.builder()
                .id(10L)
                .facilityId("FC001298")
                .facilityName("시온아트홀 (구. JK아트홀, 샘아트홀)")
                .title("아이패드 핑크")
                .foundDate(LocalDate.of(2023, 3, 4))
                .foundTime(LocalTime.of(11, 23))
                .imageUrl("image-url")
                .build();
        given(lostItemService.search(any(), any())).willReturn(new SliceImpl<>(List.of(lostItemResponse)));

        // expected
        mockMvc.perform(get("/lostItems")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
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
                        queryParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("페이지 사이즈").optional(),
                                parameterWithName("facilityId").description("공연시설 ID").optional(),
                                parameterWithName("type").description("분류").optional(),
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
                                fieldWithPath("foundTime").description("습득시간"),
                                fieldWithPath("imageUrl").description("이미지")
                        )
                ));
    }

    @Test
    @WithMockUser
    void getDetail_Docs() throws Exception {
        // given
        LostItemDetailResponse lostItemDetailResponse = LostItemDetailResponse.builder()
                .id(10L)
                .facilityId("FC001298")
                .facilityName("시온아트홀 (구. JK아트홀, 샘아트홀)")
                .facilityPhone("01-234-5678")
                .title("아이패드 핑크")
                .type(LostItemType.ELECTRONIC_EQUIPMENT)
                .foundPlaceDetail("2열")
                .foundDate(LocalDate.of(2023, 3, 4))
                .foundTime(LocalTime.of(11, 23))
                .particulars("기스있음")
                .imageUrl("image-url")
                .build();
        given(lostItemService.getDetail(any())).willReturn(lostItemDetailResponse);

        // expected
        mockMvc.perform(get("/lostItems/{lostItemId}", "10")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("lostitem-get-detail",
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
                                fieldWithPath("foundTime").description("습득시간"),
                                fieldWithPath("particulars").description("특이사항"),
                                fieldWithPath("imageUrl").description("이미지")
                        )
                ));
    }

    @Test
    @WithMockUser
    void deleteReview_Docs() throws Exception {
        // given
        given(accountService.getMemberId(any())).willReturn(5L);

        given(lostItemService.isOwnedByMember(any(), any())).willReturn(true);

        // expected
        mockMvc.perform(delete("/lostItems/{lostItemId}", "10")
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("lostitem-delete-lostitem",
                        pathParameters(
                                parameterWithName("lostItemId").description("분실물 ID")
                        )
                ));
    }
}