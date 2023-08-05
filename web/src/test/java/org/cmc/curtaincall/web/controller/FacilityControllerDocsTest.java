package org.cmc.curtaincall.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cmc.curtaincall.web.common.RestDocsConfig;
import org.cmc.curtaincall.web.service.facility.FacilityService;
import org.cmc.curtaincall.web.service.facility.response.FacilityDetailResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(RestDocsConfig.class)
@AutoConfigureRestDocs
@WebMvcTest(FacilityController.class)
class FacilityControllerDocsTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    FacilityService facilityService;

    @Test
    @WithMockUser
    void getFacilityDetail() throws Exception {
        // given
        FacilityDetailResponse response = FacilityDetailResponse.builder()
                .id("FC001298")
                .name("시온아트홀 (구. JK아트홀, 샘아트홀)")
                .hallNum(1)
                .characteristic("민간(대학로)")
                .openingYear(2017)
                .seatNum(134)
                .phone("070-8719-9106")
                .homepage("https://www.wwww.theblessed.net/")
                .address("서울특별시 종로구 대학로8가길 52 (동숭동)")
                .latitude(37.5829439)
                .longitude(127.00262709999992)
                .build();
        given(facilityService.getDetail(any())).willReturn(response);

        // expected
        mockMvc.perform(get("/facilities/{facilityId}", "FC001298")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("facility-get-facility-detail",
                        pathParameters(
                                parameterWithName("facilityId").description("공연 시설 아이디")
                        ),
                        responseFields(
                                fieldWithPath("id").description("공연장 ID"),
                                fieldWithPath("name").description("공연시설명"),
                                fieldWithPath("hallNum").description("공연장 수"),
                                fieldWithPath("characteristic").description("특징"),
                                fieldWithPath("openingYear").description("개관연도"),
                                fieldWithPath("seatNum").description("좌석수"),
                                fieldWithPath("phone").description("전화번호"),
                                fieldWithPath("homepage").description("홈페이지"),
                                fieldWithPath("address").description("주소"),
                                fieldWithPath("latitude").description("위도"),
                                fieldWithPath("longitude").description("경도")
                        )
                ));
    }
}