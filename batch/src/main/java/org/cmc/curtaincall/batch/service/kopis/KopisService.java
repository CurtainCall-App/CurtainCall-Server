package org.cmc.curtaincall.batch.service.kopis;

import org.cmc.curtaincall.batch.config.ClientLog;
import org.cmc.curtaincall.batch.exception.RequestErrorException;
import org.cmc.curtaincall.batch.service.kopis.request.FacilityListRequest;
import org.cmc.curtaincall.batch.service.kopis.response.FacilityDetailResponse;
import org.cmc.curtaincall.batch.service.kopis.response.FacilityResponse;
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
