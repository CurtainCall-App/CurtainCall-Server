package org.cmc.curtaincall.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cmc.curtaincall.domain.lostitem.LostItemType;
import org.cmc.curtaincall.web.common.RestDocsConfig;
import org.cmc.curtaincall.web.service.account.AccountService;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.image.ImageService;
import org.cmc.curtaincall.web.service.lostitem.LostItemService;
import org.cmc.curtaincall.web.service.lostitem.request.LostItemCreate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
                .foundAt(LocalDateTime.of(2023, 4, 14, 20, 11))
                .particulars("기스 많음")
                .imageId(1L)
                .build();
        given(imageService.isOwnedByMember(any(), any())).willReturn(true);
        given(lostItemService.create(any())).willReturn(new IdResult<>(10L));

        // expected
        mockMvc.perform(post("/lostitems")
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lostItemCreate))
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("lostitem-create-lostitem",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
                        requestFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("type").description("분류"),
                                fieldWithPath("facilityId").description("습득장소(공연장) ID"),
                                fieldWithPath("foundPlaceDetail").description("세부장수"),
                                fieldWithPath("foundAt").description("습득일시"),
                                fieldWithPath("particulars").description("특이사항"),
                                fieldWithPath("imageId").description("이미지 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("분실물 ID")
                        )
                ));
    }
}