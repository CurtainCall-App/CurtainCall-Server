package org.cmc.curtaincall.batch.service.kopis;

import org.cmc.curtaincall.batch.config.ClientLog;
import org.cmc.curtaincall.batch.exception.RequestErrorException;
import org.cmc.curtaincall.batch.service.kopis.request.BoxOfficeRequest;
import org.cmc.curtaincall.batch.service.kopis.request.FacilityListRequest;
import org.cmc.curtaincall.batch.service.kopis.request.ShowListRequest;
import org.cmc.curtaincall.batch.service.kopis.response.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@ClientLog
public class KopisService {

    private final String baseUrl;

    private final String serviceKey;

    private final HttpClient httpClient;

    private final DateTimeFormatter requestFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public KopisService(
            @Value("${app.kopis.service-key}") String serviceKey,
            @Value("${app.kopis.base-url}") String baseUrl) {
        this.serviceKey = serviceKey;
        this.baseUrl = baseUrl;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(2000L))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    public List<FacilityResponse> getFacilityList(final FacilityListRequest request) {
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("cpage", Integer.toString(request.page()));
        parameters.put("rows", Integer.toString(request.size()));
        Optional.ofNullable(request.name()).ifPresent(item -> parameters.put("shprfnmfct", item));
        Optional.ofNullable(request.characteristicsCode()).ifPresent(item -> parameters.put("fcltychartr", item));
        Optional.ofNullable(request.signguCode()).ifPresent(item -> parameters.put("signgucode", item));
        Optional.ofNullable(request.gugunCode()).ifPresent(item -> parameters.put("signgucodesub", item));

        final String xml = request("/openApi/restful/prfplc", parameters);
        final List<Map<String, String>> objects = convertXmlToObject(xml, "db");

        List<FacilityResponse> result = new ArrayList<>();
        for (Map<String, String> object : objects) {
            FacilityResponse facilityResponse = FacilityResponse.builder()
                    .id(object.get("mt10id").trim())
                    .name(object.get("fcltynm").trim())
                    .hallNum(Integer.parseInt(object.get("mt13cnt").trim()))
                    .characteristics(object.get("fcltychartr").trim())
                    .sido(object.get("sidonm").trim())
                    .gugun(object.get("gugunnm").trim())
                    .openingYear(Optional.of(object.get("opende").trim())
                            .filter(StringUtils::hasText).map(Integer::valueOf).orElse(null))
                    .build();
            result.add(facilityResponse);
        }
        return result;
    }

    public FacilityDetailResponse getFacilityDetail(final String id) {
        final String xml = request("/openApi/restful/prfplc/" + id);
        final List<Map<String, String>> objects = convertXmlToObject(xml, "db");
        final Map<String, String> object = objects.get(0);

        return FacilityDetailResponse.builder()
                .id(object.get("mt10id").trim())
                .name(object.get("fcltynm").trim())
                .hallNum(Integer.parseInt(object.get("mt13cnt").trim()))
                .characteristics(object.get("fcltychartr").trim())
                .openingYear(Optional.of(object.get("opende").trim())
                        .filter(StringUtils::hasText).map(Integer::valueOf).orElse(null))
                .seatNum(Integer.parseInt(object.get("seatscale")))
                .phone(object.get("telno").trim())
                .homepage(object.get("relateurl").trim())
                .address(object.get("adres").trim())
                .latitude(Double.parseDouble(object.get("la").trim()))
                .longitude(Double.parseDouble(object.get("lo").trim()))
                .build();
    }

    public List<ShowResponse> getShowList(final ShowListRequest request) {
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("cpage", Integer.toString(request.page()));
        parameters.put("rows", Integer.toString(request.size()));
        parameters.put("stdate", request.startDate().format(requestFormatter));
        parameters.put("eddate", request.endDate().format(requestFormatter));
        Optional.ofNullable(request.name()).ifPresent(item -> parameters.put("shprfnm", item));
        Optional.ofNullable(request.facilityName()).ifPresent(item -> parameters.put("shprfnmfct", item));
        Optional.ofNullable(request.genre()).ifPresent(item -> parameters.put("shcate", item.getCode()));
        Optional.ofNullable(request.facilityCode()).ifPresent(item -> parameters.put("prfplccd", item));
        Optional.ofNullable(request.sido()).ifPresent(item -> parameters.put("signgucode", item));
        Optional.ofNullable(request.gugun()).ifPresent(item -> parameters.put("signgucodesub", item));
        Optional.ofNullable(request.state()).ifPresent(item -> parameters.put("prfstate", item));
        Optional.ofNullable(request.openRun()).ifPresent(item -> parameters.put("openrun", item));

        final String xml = request("/openApi/restful/pblprfr", parameters);
        final List<Map<String, String>> objects = convertXmlToObject(xml, "db");

        List<ShowResponse> result = new ArrayList<>();
        for (Map<String, String> object : objects) {
            ShowResponse showResponse = ShowResponse.builder()
                    .id(object.get("mt20id").trim())
                    .name(object.get("prfnm").trim())
                    .genreName(object.get("genrenm").trim())
                    .state(object.get("prfstate").trim())
                    .state(object.get("prfstate").trim())
                    .startDate(object.get("prfpdfrom").trim())
                    .endDate(object.get("prfpdto").trim())
                    .poster(object.get("poster").trim())
                    .facilityName(object.get("fcltynm").trim())
                    .openRun(object.get("openrun").trim())
                    .build();
            result.add(showResponse);
        }
        return result;
    }


    public ShowDetailResponse getShowDetail(final String id) {
        final String xml = request("/openApi/restful/pblprfr/" + id);
        final List<Map<String, String>> objects = convertXmlToObject(xml, "db");
        final Map<String, String> object = objects.get(0);

        List<String> introductionImages = Optional.ofNullable(object.get("styurls"))
                .map(styurls -> Arrays.stream(styurls.trim()
                                .split("\n *"))
                        .filter(StringUtils::hasText)
                        .toList())
                .orElse(Collections.emptyList());

        return ShowDetailResponse.builder()
                .id(object.get("mt20id").trim())
                .name(object.get("prfnm").trim())
                .startDate(object.get("prfpdfrom").trim())
                .endDate(object.get("prfpdto").trim())
                .facilityName(object.get("fcltynm").trim())
                .facilityId(object.get("mt10id").trim())
                .cast(object.getOrDefault("prfcast", "").trim())
                .crew(object.getOrDefault("prfcrew", "").trim())
                .runtime(object.get("prfruntime").trim())
                .age(object.getOrDefault("prfage", "").trim())
                .enterprise(object.getOrDefault("entrpsnm", "").trim())
                .price(object.getOrDefault("pcseguidance", "").trim())
                .poster(object.getOrDefault("poster", "").trim())
                .story(object.getOrDefault("sty", "").trim())
                .state(object.get("prfstate").trim())
                .genreName(object.get("genrenm").trim())
                .openRun(object.get("openrun").trim())
                .showTimes(object.get("dtguidance").trim())
                .introductionImages(introductionImages)
                .build();
    }


    public List<BoxOfficeResponse> getBoxOfficeList(BoxOfficeRequest request) {
        URI uri = URI.create(
                baseUrl + "/openApi/restful/boxoffice"
                        + "?" + "service=" + serviceKey
                        + "&" + "date=" + request.baseDate().format(requestFormatter)
                        + "&" + "ststype=" + request.type().name().toLowerCase()
                        + Optional.ofNullable(request.genre().getCode())
                        .map(genreCode -> "&" + "catecode=" + genreCode)
                        .orElse("")
        );
        HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                .GET()
                .build();

        List<BoxOfficeResponse> result = new ArrayList<>();
        try {
            String response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body();
            InputSource is = new InputSource(new StringReader(response));
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
            NodeList nodes = document.getElementsByTagName("boxof");
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                Map<String, String> tagToValue = convertXmlNodeToMap(node);
                BoxOfficeResponse boxOfficeResponse = BoxOfficeResponse.builder()
                        .showId(tagToValue.get("mt20id").trim())
                        .showName(tagToValue.get("prfnm").trim())
                        .facilityName(tagToValue.get("prfplcnm").trim())
                        .rank(Integer.parseInt(tagToValue.get("rnum").trim()))
                        .seatNum(Integer.parseInt(tagToValue.get("seatcnt").trim()))
                        .showPeriod(tagToValue.get("prfpd").trim())
                        .poster(tagToValue.get("poster").trim())
                        .genreName(tagToValue.get("cate").trim())
                        .showCount(Integer.parseInt(tagToValue.get("prfdtcnt").trim()))
                        .area(tagToValue.getOrDefault("area", "").trim())
                        .build();
                result.add(boxOfficeResponse);
            }

        } catch (Exception e) {
            throw new RequestErrorException(e);
        }
        return result;
    }


    private Map<String, String> convertXmlNodeToMap(Node node) {
        Map<String, String> tagToValue = new HashMap<>();
        NodeList nodeValues = node.getChildNodes();
        for (int i = 0; i < nodeValues.getLength(); i++) {
            Node nodeValue = nodeValues.item(i);
            tagToValue.put(nodeValue.getNodeName(), nodeValue.getTextContent());
        }
        return tagToValue;
    }
    private String request(final String path) {
        return request(path, Collections.emptyMap());
    }

    private String request(final String path, final Map<String, String> parameters) {
        final StringBuilder sb = new StringBuilder(baseUrl);
        sb.append(path);
        sb.append("?").append("service").append("=").append(serviceKey);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }
        final URI uri = URI.create(sb.toString());
        HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                .GET()
                .build();
        try {
            final HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if ((httpResponse.statusCode() / 100) == 4) {
                throw new IllegalArgumentException(httpResponse + "\n" + httpResponse.body());
            }
            return httpResponse.body();
        } catch (IOException | InterruptedException e) {
            throw new RequestErrorException(e);
        }
    }

    private List<Map<String, String>> convertXmlToObject(final String xml, final String objectTag) {
        final Pattern objectPattern = Pattern.compile("<" + objectTag + ">(?<object>.*?)</" + objectTag + ">");
        final Matcher objectMatcher = objectPattern.matcher(xml.replace("\n", ""));
        final List<Map<String, String>> objctList = new ArrayList<>();

        while (objectMatcher.find()) {
            final String xmlObject = objectMatcher.group("object");
            final Pattern fieldValuePattern = Pattern.compile("<(?<field>[A-Za-z0-9]+)>(?<value>.*?)</\\1>");
            final Matcher fieldValueMatcher = fieldValuePattern.matcher(xmlObject);
            final Map<String, String> objectMap = new HashMap<>();
            while (fieldValueMatcher.find()) {
                final String field = fieldValueMatcher.group("field");
                final String value = fieldValueMatcher.group("value");
                objectMap.put(field, value.trim());
            }
            objctList.add(Collections.unmodifiableMap(objectMap));
        }

        return Collections.unmodifiableList(objctList);
    }
}
