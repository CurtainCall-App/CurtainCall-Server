package org.cmc.curtaincall.web.show.infra;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import org.cmc.curtaincall.domain.show.ShowId;

public class ShowIdXmlAdapter extends XmlAdapter<String, ShowId> {

    @Override
    public ShowId unmarshal(final String v) throws Exception {
        return new ShowId(v);
    }

    @Override
    public String marshal(final ShowId v) throws Exception {
        return v.getId();
    }
}
