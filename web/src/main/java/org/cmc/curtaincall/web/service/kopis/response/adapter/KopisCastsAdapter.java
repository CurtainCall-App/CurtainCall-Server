package org.cmc.curtaincall.web.service.kopis.response.adapter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.util.Arrays;
import java.util.List;

public class KopisCastsAdapter extends XmlAdapter<String, List<String>> {

    @Override
    public List<String> unmarshal(String v) throws Exception {
        return Arrays.stream(v
                        .trim()
                        .replaceAll(" 등$", "")
                        .split(",\\s*"))
                .toList();
    }

    @Override
    public String marshal(List<String> v) throws Exception {
        return String.join(", ", v);
    }
}
