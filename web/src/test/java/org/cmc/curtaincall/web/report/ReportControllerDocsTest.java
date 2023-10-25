package org.cmc.curtaincall.web.report;

import org.cmc.curtaincall.domain.report.ReportReason;
import org.cmc.curtaincall.domain.report.ReportType;
import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.report.request.ReportCreate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.cmc.curtaincall.web.common.RestDocsAttribute.constraint;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
class ReportControllerDocsTest extends AbstractWebTest {

    @MockBean
    private ReportService reportService;

    @Test
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
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reportCreate))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("report-create-report",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 필요")
                        ),
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