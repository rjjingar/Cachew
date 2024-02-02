package org.cachew.cache;

import org.cachew.cache.SourceRetriever;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SourceRetrieverSample implements SourceRetriever<String, String> {

    private final Map<String, String> storageMap;

    public SourceRetrieverSample(Map<String, String> map) {
        this.storageMap = map;
    }

    @Override
    public Optional<String> retriveValue(String key) {
        if (storageMap.containsKey(key)) {
            return Optional.of(storageMap.get(key));
        }
        return Optional.empty();
    }
}
