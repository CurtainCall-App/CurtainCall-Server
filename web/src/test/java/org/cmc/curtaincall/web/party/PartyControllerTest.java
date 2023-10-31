package org.cmc.curtaincall.web.party;

import org.cmc.curtaincall.domain.party.PartyCategory;
import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.common.response.IdResult;
import org.cmc.curtaincall.web.party.request.PartyCreate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PartyController.class)
class PartyControllerTest extends AbstractWebTest {

    @MockBean
    private PartyService partyService;

    @Test
    @DisplayName("파티 생성 - 기타 파티 아닌 경우 showId not null 검증")
    void createParty_when_NotEtcCategory_then_showIdNotNull() throws Exception {
        // given
        PartyCreate partyCreate = PartyCreate.builder()
                .showId(null)
                .showAt(LocalDateTime.of(2023, 4, 28, 19, 30))
                .title("공연 같이 보실분~")
                .content("저랑 같이 봐요~")
                .maxMemberNum(5)
                .category(PartyCategory.WATCHING)
                .build();
        given(partyService.create(any())).willReturn(new IdResult<>(10L));

        // expected
        mockMvc.perform(post("/parties")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partyCreate))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("파티 생성 - 기타 파티 아닌 경우 showAt not null 검증")
    void createParty_when_NotEtcCategory_then_showAtNotNull() throws Exception {
        // given
        PartyCreate partyCreate = PartyCreate.builder()
                .showId("PF220846")
                .showAt(null)
                .title("공연 같이 보실분~")
                .content("저랑 같이 봐요~")
                .maxMemberNum(5)
                .category(PartyCategory.WATCHING)
                .build();
        given(partyService.create(any())).willReturn(new IdResult<>(10L));

        // expected
        mockMvc.perform(post("/parties")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partyCreate))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("파티 생성 - 기타 파티 아닌 경우 최대 파티원 수 10")
    void createParty_when_NotEtcCategory_then_maxMemberNumLessOrEqual10() throws Exception {
        // given
        PartyCreate partyCreate = PartyCreate.builder()
                .showId("PF220846")
                .showAt(LocalDateTime.of(2023, 4, 28, 19, 30))
                .title("공연 같이 보실분~")
                .content("저랑 같이 봐요~")
                .maxMemberNum(11)
                .category(PartyCategory.WATCHING)
                .build();
        given(partyService.create(any())).willReturn(new IdResult<>(10L));

        // expected
        mockMvc.perform(post("/parties")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partyCreate))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("파티 생성 - 기타 파티 검증")
    void createParty_when_EtcCategory() throws Exception {
        // given
        PartyCreate partyCreate = PartyCreate.builder()
                .showId(null)
                .showAt(null)
                .title("공연 같이 보실분~")
                .content("저랑 같이 봐요~")
                .maxMemberNum(11)
                .category(PartyCategory.ETC)
                .build();
        given(partyService.create(any())).willReturn(new IdResult<>(10L));

        // expected
        mockMvc.perform(post("/parties")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partyCreate))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}