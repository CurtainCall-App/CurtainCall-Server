package org.cmc.curtaincall.web.code;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.account.dao.AccountDao;
import org.cmc.curtaincall.domain.core.EnumMapperType;
import org.cmc.curtaincall.domain.lostitem.LostItemType;
import org.cmc.curtaincall.domain.member.MemberWithdrawReason;
import org.cmc.curtaincall.domain.party.PartyCategory;
import org.cmc.curtaincall.domain.report.ReportReason;
import org.cmc.curtaincall.domain.report.ReportType;
import org.cmc.curtaincall.domain.show.BoxOfficeGenre;
import org.cmc.curtaincall.domain.show.BoxOfficeType;
import org.cmc.curtaincall.domain.show.ShowDay;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(EnumMapperConfig.class)
@AutoConfigureRestDocs
@WebMvcTest(controllers = CodeController.class)
class CodeControllerDocsTest {

    private static final String CODE_TYPES_PARAM = "codeTypes";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EnumMapperFactory enumMapperFactory;

    @MockBean
    private AccountDao accountDao;

    @Test
    @DisplayName("코드 리스트 조회 API")
    @WithMockUser
    void codeApi() throws Exception {
        // given
        enumMapperFactory.put(CodeEnum.class.getSimpleName(), CodeEnum.class);

        // expected
        mockMvc.perform(get("/code")
                        .accept(MediaType.APPLICATION_JSON)
                        .param(CODE_TYPES_PARAM, "CodeEnum"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("code-api",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("codeTypes").description("조회하려는 코드 타입들(리스트)")
                        ),
                        responseFields(
                                fieldWithPath("CodeEnum").description("코드 타입"),
                                fieldWithPath("CodeEnum[].code").description("코드"),
                                fieldWithPath("CodeEnum[].title").description("코드 제목")
                        )
                ));
    }

    /**
     * 코드 (EnumMapperType) 가 추가될 경우 테스트에 추가
     */
    @Test
    @DisplayName("코드 리스트 전체 조회 API")
    @WithMockUser
    void codeApi_GetAll() throws Exception {
        // given

        // expected
        mockMvc.perform(get("/code")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.EnumCodeExample").isNotEmpty())  // 등록된 EnumMapperType
                .andExpect(jsonPath("$." + LostItemType.class.getSimpleName()).isNotEmpty())
                .andExpect(jsonPath("$." + PartyCategory.class.getSimpleName()).isNotEmpty())
                .andExpect(jsonPath("$." + ReportReason.class.getSimpleName()).isNotEmpty())
                .andExpect(jsonPath("$." + BoxOfficeGenre.class.getSimpleName()).isNotEmpty())
                .andExpect(jsonPath("$." + BoxOfficeType.class.getSimpleName()).isNotEmpty())
                .andExpect(jsonPath("$." + ShowDay.class.getSimpleName()).isNotEmpty())
                .andExpect(jsonPath("$." + ShowGenre.class.getSimpleName()).isNotEmpty())
                .andExpect(jsonPath("$." + MemberWithdrawReason.class.getSimpleName()).isNotEmpty())
                .andExpect(jsonPath("$." + ShowState.class.getSimpleName()).isNotEmpty())
                .andExpect(jsonPath("$." + ReportType.class.getSimpleName()).isNotEmpty())
                .andDo(document("code-api-get-all",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                //subsectionWithPath("FieldCategory").description("분야 카테고리")    // RestDocs 등록
                                subsectionWithPath(LostItemType.class.getSimpleName()).description("분실물 타입: "
                                        + Arrays.toString(LostItemType.values())),
                                subsectionWithPath(PartyCategory.class.getSimpleName()).description("파티 카테고리: "
                                        + Arrays.toString(PartyCategory.values())),
                                subsectionWithPath(ReportReason.class.getSimpleName()).description("신고사유: "
                                        + Arrays.toString(ReportReason.values())),
                                subsectionWithPath(BoxOfficeGenre.class.getSimpleName()).description("인기 순위 장르: "
                                        + Arrays.toString(BoxOfficeGenre.values())),
                                subsectionWithPath(BoxOfficeType.class.getSimpleName()).description("인기 순위 타입: "
                                        + Arrays.toString(BoxOfficeType.values())),
                                subsectionWithPath(ShowDay.class.getSimpleName()).description("공연 요일: "
                                        + Arrays.toString(ShowDay.values())),
                                subsectionWithPath(ShowGenre.class.getSimpleName()).description("공연 장르: "
                                        + Arrays.toString(ShowGenre.values())),
                                subsectionWithPath(MemberWithdrawReason.class.getSimpleName()).description("회원 탈퇴 사유: "
                                        + Arrays.toString(MemberWithdrawReason.values())),
                                subsectionWithPath(ShowState.class.getSimpleName()).description("공연 상태: "
                                        + Arrays.toString(ShowState.values())),
                                subsectionWithPath(ReportType.class.getSimpleName()).description("신고 타입: "
                                        + Arrays.toString(ReportType.values()))
                        )
                ));
    }

    @Test
    @DisplayName("코드 리스트 조회 API - 존재하지 않는 코드 타입")
    @WithMockUser
    void codeApi_CodeTypeNotExists() throws Exception {
        // given

        // expected
        mockMvc.perform(get("/code")
                        .accept(MediaType.APPLICATION_JSON)
                        .param(CODE_TYPES_PARAM, "NotFoundCode1", "NotFoundCode2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.NotFoundCode1").isEmpty())
                .andExpect(jsonPath("$.NotFoundCode2").isEmpty())
                .andDo(document("code-api-not-found"));
    }

    @RequiredArgsConstructor
    enum CodeEnum implements EnumMapperType {
        CODE("코드 이름")
        ;

        private final String title;

        @Override
        public String getCode() {
            return name();
        }

        @Override
        public String getTitle() {
            return title;
        }
    }

}