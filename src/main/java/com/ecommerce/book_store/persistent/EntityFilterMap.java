package com.ecommerce.book_store.persistent;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class EntityFilterMap {
    private Map<String, Object> filterMap;


    private void initFilterMap() {
        this.filterMap = new HashMap<>();
        this.filterMap.put("offset", 0);
        this.filterMap.put("limit", 10);
    }

    public EntityFilterMap(Map<String, Object> filterMap) {
        initFilterMap();
        this.filterMap.putAll(filterMap);
    }

    public EntityFilterMap() {
        initFilterMap();
    }

    public void addFilter(String key, Object value) {
        this.filterMap.put(key, value);
    }

    public void removeFilter(String key) {
        if (key.equals("offset") || key.equals("limit")) {
            return;
        }
        this.filterMap.remove(key);
    }


}
