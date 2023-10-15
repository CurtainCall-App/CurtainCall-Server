package org.cmc.curtaincall.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cmc.curtaincall.domain.report.ReportReason;
import org.cmc.curtaincall.domain.report.ReportType;
import org.cmc.curtaincall.web.common.RestDocsConfig;
import org.cmc.curtaincall.web.security.service.AccountService;
import org.cmc.curtaincall.web.service.report.ReportService;
import org.cmc.curtaincall.web.service.report.request.ReportCreate;
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

import static org.cmc.curtaincall.web.common.RestDocsAttribute.constraint;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(RestDocsConfig.class)
@AutoConfigureRestDocs
@WebMvcTest(ReportController.class)
class ReportControllerDocsTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AccountService accountService;

    @MockBean
    ReportService reportService;

    @Test
    @WithMockUser
    void createReport_Docs() throws Exception {
        // given
        ReportCreate reportCreate = ReportCreate.builder()
                .reason(ReportReason.HATE_SPEECH)
                .content("나쁜말 ㅠㅠ")
                .idToReport(22L)
                .type(ReportType.PARTY)
                .build();

        // expected
        mockMvc.perform(post("/reports")
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reportCreate))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("report-create-report",
                        requestFields(
                                fieldWithPath("idToReport").description("신고하려는 글 ID"),
                                fieldWithPath("type").description("신고하려는 글 유형")
                                        .type(ReportType.class.getSimpleName()),
                                fieldWithPath("reason").description("신고 이유")
                                        .type(ReportReason.class.getSimpleName()),
                                fieldWithPath("content").description("내용")
                                        .type(constraint("max=400"))
                        )
                ));
    }
}