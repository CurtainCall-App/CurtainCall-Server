package org.cmc.curtaincall.web.lostitem;

import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.lostitem.LostItemId;
import org.cmc.curtaincall.domain.lostitem.LostItemType;
import org.cmc.curtaincall.domain.lostitem.validation.LostItemCreatorValidator;
import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.common.response.IdResult;
import org.cmc.curtaincall.web.lostitem.request.LostItemCreate;
import org.cmc.curtaincall.web.lostitem.request.LostItemEdit;
import org.cmc.curtaincall.web.service.image.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.cmc.curtaincall.web.common.RestDocsAttribute.constraint;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LostItemController.class)
class LostItemControllerDocsTest extends AbstractWebTest {

    @MockBean
    private LostItemService lostItemService;

    @MockBean
    private LostItemCreatorValidator lostItemCreatorValidator;

    @MockBean
    private ImageService imageService;

    @Test
    void createLostItem_Docs() throws Exception {
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
        given(imageService.isOwnedByMember(LOGIN_MEMBER_ID.getId(), 1L)).willReturn(true);
        given(lostItemService.create(lostItemCreate)).willReturn(new IdResult<>(10L));

        // expected
        mockMvc.perform(post("/lostItems")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lostItemCreate))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andDo(document("lostitem-create-lostitem",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        requestFields(
                                fieldWithPath("title").description("제목")
                                        .attributes(key("constraint").value("최대 20")),
                                fieldWithPath("type").type(LostItemType.class.getSimpleName()).description("분류"),
                                fieldWithPath("facilityId").description("습득장소(공연장) ID"),
                                fieldWithPath("foundPlaceDetail").description("세부장수")
                                        .attributes(key("constraint").value("최대 30")),
                                fieldWithPath("foundDate").description("습득일자")
                                        .attributes(constraint("과거 혹은 오늘 날짜")),
                                fieldWithPath("foundTime").description("습득시간").optional(),
                                fieldWithPath("particulars").description("특이사항")
                                        .attributes(constraint("최대: 100")),
                                fieldWithPath("imageId").description("이미지 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("분실물 ID")
                        )
                ));
    }

    @Test
    void deleteReview_Docs() throws Exception {
        // given

        // expected
        mockMvc.perform(delete("/lostItems/{lostItemId}", "10")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("lostitem-delete-lostitem",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        pathParameters(
                                parameterWithName("lostItemId").description("분실물 ID")
                        )
                ));

        LostItemId lostItemId = new LostItemId(10L);
        then(lostItemCreatorValidator).should(times(1))
                .validate(lostItemId, new CreatorId(LOGIN_MEMBER_ID));
        then(lostItemService).should(times(1)).delete(lostItemId);
    }

    @Test
    void editLostItem_Docs() throws Exception {
        // given
        var lostItemEdit = LostItemEdit.builder()
                .title("아이폰 핑크")
                .type(LostItemType.ELECTRONIC_EQUIPMENT)
                .foundPlaceDetail("2열 8석")
                .foundDate(LocalDate.of(2023, 3, 4))
                .foundTime(LocalTime.of(11, 23))
                .particulars("기스 많음")
                .imageId(1L)
                .build();
        given(imageService.isOwnedByMember(any(), any())).willReturn(true);

        // expected
        mockMvc.perform(patch("/lostItems/{lostItemId}", 10L)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lostItemEdit))
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("lostitem-edit-lostitem",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        pathParameters(
                                parameterWithName("lostItemId").description("분실물 ID")
                        ),
                        requestFields(
                                fieldWithPath("title").description("제목")
                                        .attributes(constraint("max=20")),
                                fieldWithPath("type").type(LostItemType.class.getSimpleName()).description("분류"),
                                fieldWithPath("foundPlaceDetail").description("세부장수")
                                        .attributes(constraint("max=30")),
                                fieldWithPath("foundDate").description("습득일자")
                                        .attributes(constraint("과거 혹은 오늘 날짜")),
                                fieldWithPath("foundTime").description("습득시간")
                                        .optional(),
                                fieldWithPath("particulars").description("특이사항")
                                        .attributes(constraint("max=100")),
                                fieldWithPath("imageId").description("이미지 ID")
                        )
                ));
    }
}