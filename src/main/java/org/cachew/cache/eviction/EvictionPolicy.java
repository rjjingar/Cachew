package org.cachew.cache.eviction;

import java.time.Duration;
import java.util.Set;

public interface EvictionPolicy<T> {

    public boolean shouldApply();
    public Set<T> evictKeys();

    public boolean shouldEvictNode(T cacheNode);

    public void accessKey(T cacheNode);

    public void accessKey(T cacheNode, Duration ttl);

    public void removeKey(T cacheNode);
}
