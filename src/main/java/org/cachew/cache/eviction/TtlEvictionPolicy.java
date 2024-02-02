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
    public void resetAccess(CacheNode<K, V> cacheNode) {
        cacheNode.resetLastAccessTime();
    }

    @Override
    public void resetAccess(CacheNode<K, V> cacheNode, Duration ttl) {
        resetAccess(cacheNode);
        cacheNode.setExpiry(ttl);
    }
}
