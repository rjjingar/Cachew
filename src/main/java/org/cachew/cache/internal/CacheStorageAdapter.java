package org.cachew.cache.internal;

import lombok.RequiredArgsConstructor;
import org.cachew.cache.CacheStorage;
import org.cachew.cache.CacheOrigin;
import org.cachew.cache.error.CachewException;
import org.cachew.cache.error.OriginException;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;

import static org.cachew.cache.error.CachewException.CachewErrorCode.KEY_NOT_FOUND_CACHE;
import static org.cachew.cache.error.CachewException.CachewErrorCode.KEY_NOT_FOUND_ORIGIN;
import static org.cachew.cache.error.OriginException.OriginErrorCode.ORIGIN_NOT_SET;

@RequiredArgsConstructor
public class CacheStorageAdapter<K, V> {

    private final CacheStorage<K, V> cacheStorage;
    private final CacheOrigin<K, V> cacheOrigin;

    public CacheNode<K, V> fetch(final K key, final Duration ttl) throws CachewException {
        CacheNode<K, V> cacheNode = fetchFromCache(key);
        if (cacheNode != null && cacheNode.hasExpired()) {
            cacheStorage.remove(key);
            cacheNode = null;
        }
        if (cacheNode == null) {
            try {
                cacheNode = refreshFromSource(key, ttl);
            } catch (OriginException e) {
                if (e.getErrorCode() == ORIGIN_NOT_SET) {
                    throw new CachewException(KEY_NOT_FOUND_CACHE, String.valueOf(key));
                } else {
                    throw new CachewException(KEY_NOT_FOUND_ORIGIN);
                }
            }
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

    public CacheNode<K, V> refreshFromSource(final K key, final Duration ttl) throws OriginException {
        if (this.cacheOrigin == null) {
            throw new OriginException(ORIGIN_NOT_SET);
        }

        Optional<V> valueOptional = cacheOrigin.retrieveValue(key);
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
