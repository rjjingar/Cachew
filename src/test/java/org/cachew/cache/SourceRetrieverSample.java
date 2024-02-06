package org.cachew.cache;

import org.cachew.cache.error.OriginException;

import java.util.Map;
import java.util.Optional;

public class SourceRetrieverSample implements CacheOrigin<String, String> {

    private final Map<String, String> storageMap;

    public SourceRetrieverSample(Map<String, String> map) {
        this.storageMap = map;
    }

    @Override
    public Optional<String> retriveValue(String key) throws OriginException {
        if (storageMap.containsKey(key)) {
            return Optional.of(storageMap.get(key));
        }
        if (key.contains("invalid")) {
            throw new OriginException(OriginException.OriginErrorCode.KEY_NOT_FOUND_IN_ORIGIN);
        }
        return Optional.empty();
    }
}
