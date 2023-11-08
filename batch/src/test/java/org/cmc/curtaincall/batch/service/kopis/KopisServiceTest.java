package org.cmc.curtaincall.batch.service.kopis;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class KopisServiceTest {

    @Test
    void test() throws IOException, InterruptedException, ParserConfigurationException, SAXException {
        final HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(2000L))
                .build();
        URI uri = URI.create(
                "http://kopis.or.kr" + "/openApi/restful/prfplc"
                        + "?" + "service=" + "c20f4a906a604883a6adcce4979d3a4d"
                        + "&" + "cpage=" + "1"
                        + "&" + "rows=" + "5"
        );
        HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                .GET()
                .build();
        String response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body();
        System.out.println(response);
        final Pattern objectPattern = Pattern.compile("<db>(.*?)</db>");
        final Matcher objectMatcher = objectPattern.matcher(response);

        List<Map<String, String>> objctList = new ArrayList<>();
        while (objectMatcher.find()) {
            final String xmlObject = objectMatcher.group();
            final Pattern fieldValuePattern = Pattern.compile("<(?<field>[A-Za-z0-9]+)>(?<value>.*?)</\\1>");
            final Matcher fieldValueMatcher = fieldValuePattern.matcher(xmlObject);
            final Map<String, String> objectMap = new HashMap<>();
            while (fieldValueMatcher.find()) {
                final String field = fieldValueMatcher.group("field");
                final String value = fieldValueMatcher.group("value");
                objectMap.put(field, value);
            }
            objctList.add(Collections.unmodifiableMap(objectMap));
        }
        objctList = Collections.unmodifiableList(objctList);
        System.out.println(objctList);
    }

    @Test
    void test2() {
        final String response = """
                <?xml version="1.0" encoding="UTF-8"?>
                <dbs>
                    <db>
                        <fcltynm>(구)송정초등학교 야외운동장</fcltynm>
                        <mt10id>FC002078</mt10id>
                        <mt13cnt>1</mt13cnt>
                        <fcltychartr>기타(비공연장)</fcltychartr>
                        <sidonm>부산</sidonm>
                        <gugunnm>해운대구</gugunnm>
                        <opende> </opende>
                    </db>
                    <db>
                        <fcltynm>(재)경기문화재단</fcltynm>
                        <mt10id>FC001672</mt10id>
                        <mt13cnt>1</mt13cnt>
                        <fcltychartr>공공(기타)</fcltychartr>
                        <sidonm>경기</sidonm>
                        <gugunnm>수원시</gugunnm>
                        <opende>2001</opende>
                    </db>
                    <db>
                        <fcltynm>(재)구리시청소년수련관</fcltynm>
                        <mt10id>FC000534</mt10id>
                        <mt13cnt>1</mt13cnt>
                        <fcltychartr>공공(기타)</fcltychartr>
                        <sidonm>경기</sidonm>
                        <gugunnm>구리시</gugunnm>
                        <opende>2003</opende>
                    </db>
                    <db>
                        <fcltynm>(재)영화의전당</fcltynm>
                        <mt10id>FC000175</mt10id>
                        <mt13cnt>5</mt13cnt>
                        <fcltychartr>공공(문예회관)</fcltychartr>
                        <sidonm>부산</sidonm>
                        <gugunnm>해운대구</gugunnm>
                        <opende>2011</opende>
                    </db>
                    <db>
                        <fcltynm>13블럭 소극장</fcltynm>
                        <mt10id>FC003668</mt10id>
                        <mt13cnt>1</mt13cnt>
                        <fcltychartr>민간(대학로 외)</fcltychartr>
                        <sidonm>경기</sidonm>
                        <gugunnm>고양시</gugunnm>
                        <opende>2023</opende>
                    </db>
                </dbs>
                """.replace("\n", "");
        final Pattern objectPattern = Pattern.compile("<db>(?<object>.*?)</db>");
        final Matcher objectMatcher = objectPattern.matcher(response);

        List<Map<String, String>> objctList = new ArrayList<>();
        while (objectMatcher.find()) {
            final String xmlObject = objectMatcher.group("object");
            final Pattern fieldValuePattern = Pattern.compile("<(?<field>[A-Za-z0-9]+)>(?<value>.*?)</\\1>");
            final Matcher fieldValueMatcher = fieldValuePattern.matcher(xmlObject);
            final Map<String, String> objectMap = new HashMap<>();
            while (fieldValueMatcher.find()) {
                final String field = fieldValueMatcher.group("field");
                final String value = fieldValueMatcher.group("value");
                objectMap.put(field, value);
            }
            objctList.add(Collections.unmodifiableMap(objectMap));
        }
        objctList = Collections.unmodifiableList(objctList);
        System.out.println(objctList);
    }

    @Test
    void test3() {
        final KopisService kopisService = new KopisService("c20f4a906a604883a6adcce4979d3a4d", "http://www.kopis.or.kr");
        System.out.println(kopisService.getFacilityDetail("FC003668"));
        System.out.println(kopisService.getFacilityDetail("FC003672"));
    }
}