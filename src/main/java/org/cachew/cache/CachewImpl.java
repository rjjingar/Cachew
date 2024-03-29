package org.cachew.cache;

import lombok.RequiredArgsConstructor;
import org.cachew.cache.error.CachewException;
import org.cachew.cache.error.OriginException;
import org.cachew.cache.eviction.EvictionPolicy;
import org.cachew.cache.internal.CacheConfiguration;
import org.cachew.cache.internal.CacheNode;
import org.cachew.cache.internal.CacheStorageAdapter;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CachewImpl<K, V> implements Cachew<K, V> {

    private final CacheConfiguration configuration;
    private final CacheStorageAdapter<K, V> adapter;
    private final List<EvictionPolicy<CacheNode<K, V>>> evictionPolicies;

    @Override
    public void clean() {
        this.adapter.clearStorage();
    }

    @Override
    public V getValue(K key) throws CachewException {
        CacheNode<K, V> cacheNode = adapter.fetch(key, configuration.getGlobalTtlDuration());
        if (cacheNode != null) {
            resetAccess(cacheNode);
            return cacheNode.getValue();
        }
        return null;
    }

    @Override
    public void putValue(K key, V val) {
        putValue(key, val, configuration.getGlobalTtlDuration());
    }

    @Override
    public void putValue(K key, V val, Duration ttl) {
        CacheNode<K, V> cacheNode = adapter.putInCache(key, val, ttl);
        resetAccess(cacheNode);
        evictIfNeeded();
    }

    @Override
    public V refreshValue(K key) throws OriginException {
        return refreshValue(key, configuration.getGlobalTtlDuration());
    }

    @Override
    public V refreshValue(final K key, final Duration ttl) throws OriginException {
        CacheNode<K, V> cacheNode = adapter.refreshFromSource(key, ttl);
        if (cacheNode != null) {
            resetAccess(cacheNode);
            return cacheNode.getValue();
        }
        return null;
    }

    private void evictIfNeeded() {
        Set<K> keysToEvict =
                evictionPolicies.stream()
                        .filter(EvictionPolicy::shouldApply)
                        .map(EvictionPolicy::evictKeys)
                        .flatMap(Collection::stream)
                        .map(CacheNode::getKey)
                        .collect(Collectors.toSet());
        adapter.clearStorage(keysToEvict);
    }

    private void resetAccess(CacheNode<K, V> cacheNode) {
        evictionPolicies.forEach(e -> e.accessKey(cacheNode, configuration.getGlobalTtlDuration()));
    }


}
