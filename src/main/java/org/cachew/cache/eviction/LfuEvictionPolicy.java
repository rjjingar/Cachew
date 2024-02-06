package org.cachew.cache.eviction;

import org.cachew.cache.error.CachewException;
import org.cachew.cache.internal.CacheNode;

import java.time.Duration;
import java.util.*;

/**
 * Least Frequently Used cache eviction policy.
 * If cache size crosses max limit then least frequently used cache nodes are removed.
 *
 * @param <K>
 * @param <V>
 */
public class LfuEvictionPolicy <K, V> implements EvictionPolicy<CacheNode<K, V>> {

    private final int maxCacheSize;
    private final PriorityQueue<CacheNode<K, V>> minHeap;

    private long maxFreq;
    private long minFreq;

    public LfuEvictionPolicy(int maxCacheSize) throws CachewException {
        if (maxCacheSize < 2) {
            throw CachewException.LRU_INIT_ERROR;
        }
        this.maxCacheSize = maxCacheSize;
        this.minHeap = new PriorityQueue<>(this::compareCacheNodes);
    }

    private int compareCacheNodes(CacheNode<K, V> first, CacheNode<K, V> second) {
        if (first.getAccessedCount() == second.getAccessedCount()) {
            return Long.compare(first.getLastAccessedTime(), second.getLastAccessedTime());
        }
        return Long.compare(first.getAccessedCount(), second.getAccessedCount());
    }

    @Override
    public boolean shouldApply() {
        return this.minHeap.size() > maxCacheSize;
    }

    @Override
    public Set<CacheNode<K, V>> evictKeys() {
        Set<CacheNode<K, V>> keysEvicted = new HashSet<>();
        while (this.minHeap.size() > maxCacheSize) {
            keysEvicted.add(this.minHeap.poll());
        }
        return keysEvicted;
    }

    @Override
    public boolean shouldEvictNode(CacheNode<K, V> cacheNode) {
        return false;
    }

    @Override
    public void accessKey(CacheNode<K, V> cacheNode) {
        cacheNode.incrementAccessCount();
        if (!this.minHeap.isEmpty()) {
            // not optimized
            this.minHeap.remove(cacheNode); // O (n)

        }
        this.minHeap.offer(cacheNode); // O (log n)
    }

    @Override
    public void accessKey(CacheNode<K, V> cacheNode, Duration ttl) {
        accessKey(cacheNode);
    }

    @Override
    public void removeKey(CacheNode<K, V> cacheNode) {
        this.minHeap.remove(cacheNode);
    }
}
