package org.cachew.cache.eviction;

import org.cachew.cache.internal.CacheNode;
import sun.security.util.Cache;

import java.time.Duration;
import java.util.Set;

public interface EvictionPolicy<T> {

    public boolean shouldApply();
    public Set<T> evictKeys();

    public boolean shouldEvictNode(T cacheNode);

    public void resetAccess(T cacheNode);

    public void resetAccess(T cacheNode, Duration ttl);
}
