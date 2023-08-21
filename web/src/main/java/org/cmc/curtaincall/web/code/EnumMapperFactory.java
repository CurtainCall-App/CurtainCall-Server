package org.cmc.curtaincall.web.code;


import org.cmc.curtaincall.domain.core.EnumMapperType;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EnumMapperFactory {

    private final Map<String, List<EnumMapperValue>> factory = new LinkedHashMap<>();

    public void put(String key, Class<? extends EnumMapperType> e) {
        factory.put(key, toEnumValues(e));
    }

    private List<EnumMapperValue> toEnumValues(Class<? extends EnumMapperType> e) {
        return Arrays.stream(e.getEnumConstants())
                .map(EnumMapperValue::new)
                .collect(Collectors.toList());
    }

    public List<EnumMapperValue> get(String key){
        return factory.getOrDefault(key, Collections.emptyList());
    }

    public Map<String, List<EnumMapperValue>> get(List<String> keys) {
        if(keys == null || keys.isEmpty()){
            return new LinkedHashMap<>();
        }
        return keys.stream()
                .collect(Collectors.toMap(Function.identity(), this::get));
    }

    public Map<String, List<EnumMapperValue>> getAll() {
        return factory;
    }
}
