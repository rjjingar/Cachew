package org.cachew.cache;

import java.util.Optional;

public interface SourceRetriever<K, V> {

    public Optional<V> retriveValue(K key);
}
