package com.ecommerce.book_store.persistent;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class EntityFilterMap {
    private Map<String, Object> filterMap;
    @Setter
    private List<String> filterableKeys;


    private void initFilterMap() {
        this.filterMap = new HashMap<>();
    }

    public EntityFilterMap(Map<String, Object> filterMap) {
        initFilterMap();
        this.filterMap.putAll(filterMap);
    }

    public EntityFilterMap() {
        initFilterMap();
    }

    public void addFilter(String key, Object value) {
        if (!this.filterableKeys.contains(key)) {
            throw new IllegalArgumentException("Key is not filterable");
        }
        this.filterMap.put(key, value);
    }

    public void removeFilter(String key) {
        this.filterMap.remove(key);
    }
}
