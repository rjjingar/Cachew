package org.cachew.cache.eviction;

import org.cachew.cache.internal.CacheNode;

import java.time.Duration;
import java.util.Collections;
import java.util.Set;

public class TtlEvictionPolicy<K, V> implements EvictionPolicy<CacheNode<K, V>> {

    @Override
    public boolean shouldApply() {
        return false;
    }

    @Override
    public Set<CacheNode<K, V>> evictKeys() {
        return Collections.emptySet();
    }

    @Override
    public boolean shouldEvictNode(CacheNode<K, V> cacheNode) {
        if (cacheNode == null) {
            return false;
        }
        return cacheNode.hasExpired();
    }

    @Override
    public void accessKey(CacheNode<K, V> cacheNode) {
        cacheNode.resetAccessTime();
    }

    @Override
    public void accessKey(CacheNode<K, V> cacheNode, Duration ttl) {
        accessKey(cacheNode);
        cacheNode.setExpiry(ttl);
    }

    @Override
    public void removeKey(CacheNode<K, V> cacheNode) {
        // TTL eviction policy does not need to remove this node
        // as there is no auxiliary storage
    }
}
