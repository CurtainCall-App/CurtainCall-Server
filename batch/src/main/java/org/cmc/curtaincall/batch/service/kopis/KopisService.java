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
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
                .build();
    }

    public List<FacilityResponse> getFacilityList(FacilityListRequest request) {
        URI uri = URI.create(
                baseUrl + "/openApi/restful/prfplc"
                        + "?" + "service=" + serviceKey
                        + "&" + "cpage=" + request.page()
                        + "&" + "rows=" + request.size()
        );
        HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                .GET()
                .build();


        List<FacilityResponse> result = new ArrayList<>();
        try {
            String response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body();
            InputSource is = new InputSource(new StringReader(response));
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
            NodeList nodes = document.getElementsByTagName("db");
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                Map<String, String> tagToValue = convertXmlNodeToMap(node);
                FacilityResponse facilityResponse = FacilityResponse.builder()
                        .id(tagToValue.get("mt10id").trim())
                        .name(tagToValue.get("fcltynm").trim())
                        .hallNum(Integer.parseInt(tagToValue.get("mt13cnt").trim()))
                        .characteristics(tagToValue.get("fcltychartr").trim())
                        .sido(tagToValue.get("sidonm").trim())
                        .gugun(tagToValue.get("gugunnm").trim())
                        .openingYear(Optional.of(tagToValue.get("opende").trim())
                                .filter(StringUtils::hasText).map(Integer::valueOf).orElse(null))
                        .build();
                result.add(facilityResponse);
            }

        } catch (Exception e) {
            throw new RequestErrorException(e);
        }
        return result;
    }

    public FacilityDetailResponse getFacilityDetail(String id) {
        URI uri = URI.create(
                baseUrl + "/openApi/restful/prfplc/" + id
                        + "?" + "service=" + serviceKey
        );
        HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                .GET()
                .build();

        try {
            String response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body();
            InputSource is = new InputSource(new StringReader(response));
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
            Node node = document.getElementsByTagName("db").item(0);
            Map<String, String> tagToValue = convertXmlNodeToMap(node);
            return FacilityDetailResponse.builder()
                    .id(tagToValue.get("mt10id").trim())
                    .name(tagToValue.get("fcltynm").trim())
                    .hallNum(Integer.parseInt(tagToValue.get("mt13cnt").trim()))
                    .characteristics(tagToValue.get("fcltychartr").trim())
                    .openingYear(Optional.of(tagToValue.get("opende").trim())
                            .filter(StringUtils::hasText).map(Integer::valueOf).orElse(null))
                    .seatNum(Integer.parseInt(tagToValue.get("seatscale")))
                    .phone(tagToValue.get("telno").trim())
                    .homepage(tagToValue.get("relateurl").trim())
                    .address(tagToValue.get("adres").trim())
                    .latitude(Double.parseDouble(tagToValue.get("la").trim()))
                    .longitude(Double.parseDouble(tagToValue.get("lo").trim()))
                    .build();
        } catch (Exception e) {
            throw new RequestErrorException(e);
        }
    }


    public List<ShowResponse> getShowList(ShowListRequest request) {
        URI uri = URI.create(
                baseUrl + "/openApi/restful/pblprfr"
                        + "?" + "service=" + serviceKey
                        + "&" + "cpage=" + request.page()
                        + "&" + "rows=" + request.size()
                        + "&" + "stdate=" + request.startDate().format(requestFormatter)
                        + "&" + "eddate=" + request.endDate().format(requestFormatter)
        );
        HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                .GET()
                .build();

        List<ShowResponse> result = new ArrayList<>();
        try {
            String response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body();
            InputSource is = new InputSource(new StringReader(response));
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
            NodeList nodes = document.getElementsByTagName("db");
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                Map<String, String> tagToValue = convertXmlNodeToMap(node);
                ShowResponse showResponse = ShowResponse.builder()
                        .id(tagToValue.get("mt20id").trim())
                        .name(tagToValue.get("prfnm").trim())
                        .genreName(tagToValue.get("genrenm").trim())
                        .state(tagToValue.get("prfstate").trim())
                        .state(tagToValue.get("prfstate").trim())
                        .startDate(tagToValue.get("prfpdfrom").trim())
                        .endDate(tagToValue.get("prfpdto").trim())
                        .poster(tagToValue.get("poster").trim())
                        .facilityName(tagToValue.get("fcltynm").trim())
                        .openRun(tagToValue.get("openrun").trim())
                        .build();
                result.add(showResponse);
            }

        } catch (Exception e) {
            throw new RequestErrorException(e);
        }
        return result;
    }


    public ShowDetailResponse getShowDetail(String id) {
        URI uri = URI.create(
                baseUrl + "/openApi/restful/pblprfr/" + id
                        + "?" + "service=" + serviceKey
        );
        HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                .GET()
                .build();

        try {
            String response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body();
            InputSource is = new InputSource(new StringReader(response));
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
            Node node = document.getElementsByTagName("db").item(0);
            Map<String, String> tagToValue = convertXmlNodeToMap(node);
            List<String> introductionImages = Optional.ofNullable(tagToValue.get("styurls"))
                    .map(styurls -> Arrays.stream(styurls.trim()
                                    .split("\n *"))
                            .filter(StringUtils::hasText)
                            .toList())
                    .orElse(Collections.emptyList());
            return ShowDetailResponse.builder()
                    .id(tagToValue.get("mt20id").trim())
                    .name(tagToValue.get("prfnm").trim())
                    .startDate(tagToValue.get("prfpdfrom").trim())
                    .endDate(tagToValue.get("prfpdto").trim())
                    .facilityName(tagToValue.get("fcltynm").trim())
                    .facilityId(tagToValue.get("mt10id").trim())
                    .cast(tagToValue.getOrDefault("prfcast", "").trim())
                    .crew(tagToValue.getOrDefault("prfcrew", "").trim())
                    .runtime(tagToValue.get("prfruntime").trim())
                    .age(tagToValue.getOrDefault("prfage", "").trim())
                    .enterprise(tagToValue.getOrDefault("entrpsnm", "").trim())
                    .price(tagToValue.getOrDefault("pcseguidance", "").trim())
                    .poster(tagToValue.getOrDefault("poster", "").trim())
                    .story(tagToValue.getOrDefault("sty", "").trim())
                    .state(tagToValue.get("prfstate").trim())
                    .genreName(tagToValue.get("genrenm").trim())
                    .openRun(tagToValue.get("openrun").trim())
                    .showTimes(tagToValue.get("dtguidance").trim())
                    .introductionImages(introductionImages)
                    .build();
        } catch (Exception e) {
            throw new RequestErrorException(e);
        }
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
}
