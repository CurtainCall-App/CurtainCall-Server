package org.cmc.curtaincall.web.boxoffice.infra;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.cmc.curtaincall.domain.show.BoxOfficeType;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.ShowGenre;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.domain.show.repository.ShowRepository;
import org.cmc.curtaincall.web.boxoffice.dto.BoxOfficeRequest;
import org.cmc.curtaincall.web.boxoffice.dto.BoxOfficeResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
class KopisBoxOfficeServiceTest {

    static MockWebServer mockWebServer;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    private ShowRepository showRepository;

    private KopisBoxOfficeService kopisBoxOfficeService;

    @BeforeEach
    void init() {
        showRepository = mock(ShowRepository.class);
        kopisBoxOfficeService = new KopisBoxOfficeService(
                String.format("http://localhost:%s", mockWebServer.getPort()),
                "test-service-key",
                showRepository
        );
    }

    @Test
    void getList() throws InterruptedException {
        // given
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        <?xml version="1.0" encoding="UTF-8"?>
                        <boxofs>
                            <boxof>
                                <prfplcnm>충무아트센터 대공연장</prfplcnm>
                                <seatcnt>1250</seatcnt>
                                <rnum>4</rnum>
                                <poster>/upload/pfmPoster/PF_PF227565_231012_095437.gif</poster>
                                <prfpd>2023.11.21~2024.02.25</prfpd>
                                <mt20id>PF227565</mt20id>
                                <prfnm>몬테크리스토</prfnm>
                                <cate>뮤지컬</cate>
                                <prfdtcnt>56</prfdtcnt>
                                <area>서울</area>
                            </boxof>
                        </boxofs>
                        """)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
        );

        final Show show = mock(Show.class);
        given(show.getId()).willReturn("PF227565");
        given(show.getName()).willReturn("name");
        given(show.getStartDate()).willReturn(LocalDate.of(2023, 11, 9));
        given(show.getEndDate()).willReturn(LocalDate.of(2023, 11, 10));
        given(show.getPoster()).willReturn("poster");
        given(show.getGenre()).willReturn(ShowGenre.MUSICAL);
        given(show.getReviewCount()).willReturn(10);
        given(show.getReviewGradeSum()).willReturn(45L);
        given(showRepository.findAllById(List.of("PF227565"))).willReturn(List.of(show));

        // when
        final BoxOfficeRequest request = new BoxOfficeRequest(
                BoxOfficeType.WEEK, LocalDate.of(2023, 11, 9), null, null);
        final List<BoxOfficeResponse> result = kopisBoxOfficeService.getList(request);

        // then
        assertThat(result).hasSize(1);

        final BoxOfficeResponse boxOfficeResponse = result.get(0);
        assertThat(boxOfficeResponse.id()).isEqualTo(new ShowId("PF227565"));
        assertThat(boxOfficeResponse.name()).isEqualTo("name");
        assertThat(boxOfficeResponse.startDate()).isEqualTo(LocalDate.of(2023, 11, 9));
        assertThat(boxOfficeResponse.endDate()).isEqualTo(LocalDate.of(2023, 11, 10));
        assertThat(boxOfficeResponse.poster()).isEqualTo("poster");
        assertThat(boxOfficeResponse.genre()).isEqualTo(ShowGenre.MUSICAL);
        assertThat(boxOfficeResponse.reviewGradeSum()).isEqualTo(45L);
        assertThat(boxOfficeResponse.reviewCount()).isEqualTo(10);
        assertThat(boxOfficeResponse.rank()).isEqualTo(4);

        final RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertThat(recordedRequest.getMethod()).isEqualTo(HttpMethod.GET.name());
        assertThat(URI.create(recordedRequest.getPath())).isEqualTo(
                UriComponentsBuilder.fromPath("/openApi/restful/boxoffice")
                        .queryParam("service", "test-service-key")
                        .queryParam("ststype", "week")
                        .queryParam("date", "20231109")
                        .build().toUri()
        );
    }

    @Test
    void getList_given_not_exists_show_then_skip() {
        // given
        mockWebServer.enqueue(new MockResponse()
                .setBody("""
                        <?xml version="1.0" encoding="UTF-8"?>
                        <boxofs>
                            <boxof>
                                <prfplcnm>충무아트센터 대공연장</prfplcnm>
                                <seatcnt>1250</seatcnt>
                                <rnum>4</rnum>
                                <poster>/upload/pfmPoster/PF_PF227565_231012_095437.gif</poster>
                                <prfpd>2023.11.21~2024.02.25</prfpd>
                                <mt20id>PF227565</mt20id>
                                <prfnm>몬테크리스토</prfnm>
                                <cate>뮤지컬</cate>
                                <prfdtcnt>56</prfdtcnt>
                                <area>서울</area>
                            </boxof>
                        </boxofs>
                        """)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
        );
        given(showRepository.findAllById(List.of("PF227565"))).willReturn(Collections.emptyList());

        // when
        final BoxOfficeRequest request = new BoxOfficeRequest(
                BoxOfficeType.WEEK, LocalDate.of(2023, 11, 9), null, null);
        final List<BoxOfficeResponse> result = kopisBoxOfficeService.getList(request);

        // then
        assertThat(result).isEmpty();
    }
}