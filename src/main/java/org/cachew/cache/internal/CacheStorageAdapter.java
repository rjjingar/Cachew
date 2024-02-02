package org.cachew.cache.internal;

import lombok.RequiredArgsConstructor;
import org.cachew.cache.CacheStorage;
import org.cachew.cache.SourceRetriever;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class CacheStorageAdapter<K, V> {

    private final CacheStorage<K, V> cacheStorage;
    private final SourceRetriever<K, V> retriever;

    public CacheNode<K, V> fetch(final K key, final Duration ttl) {
        CacheNode<K, V> cacheNode = fetchFromCache(key);
        if (cacheNode == null) {
            return refreshFromSource(key, ttl);
        }
        return cacheNode;
    }

    public CacheNode<K, V> putInCache(K key, V value, Duration ttl) {
        CacheNode<K, V> cacheNode = fetchFromCache(key);
        if (cacheNode == null) {
            cacheNode = cacheStorage.setValue(key, value);
            cacheNode.setExpiry(ttl);
        } else {
            cacheNode.setValue(value);
            cacheNode.setExpiry(ttl);
        }
        return cacheNode;
    }

    public CacheNode<K, V> refreshFromSource(final K key, final Duration ttl) {
        if (this.retriever == null) {
            return null;
        }
        Optional<V> valueOptional = retriever.retriveValue(key);
        V valueFromSource = valueOptional.orElse(null);
        CacheNode<K, V> cacheNode = cacheStorage.setValue(key, valueFromSource);
        if (ttl != null) {
            cacheNode.setExpiry(ttl);
        }
        return cacheNode;
    }

    public CacheNode<K, V> fetchFromCache(final K key) {
        return cacheStorage.getCacheNode(key);
    }

    public void clearStorage() {
        cacheStorage.clearStorage();
    }

    public void clearStorage(Set<K> keys) {
        if (keys == null || keys.isEmpty()) {
            return;
        }
        keys.forEach(cacheStorage::remove);
    }


}
