package org.cmc.curtaincall.web.common;

import org.springframework.restdocs.snippet.Attributes;

import static org.springframework.restdocs.snippet.Attributes.key;

public final class RestDocsAttribute {

    public static Attributes.Attribute constraint(String value) {
        return key("constraint").value(value);
    }

    public static Attributes.Attribute type(Class<?> clazz) {
        return key("type").value(clazz.getSimpleName());
    }

    public static Attributes.Attribute defaultValue(String value) {
        return key("defaultValue").value(value);
    }

    private RestDocsAttribute() {
        throw new UnsupportedOperationException();
    }
}
