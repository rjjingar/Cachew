package org.cachew.cache;

import org.cachew.cache.error.OriginException;

import java.util.Optional;

public interface CacheOrigin<K, V> {

    public Optional<V> retriveValue(K key) throws OriginException;
}
