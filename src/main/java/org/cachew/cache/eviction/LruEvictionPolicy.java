package org.cachew.cache.eviction;

import org.cachew.cache.internal.CacheNode;
import org.cachew.cache.list.IndexedLinkedList;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class LruEvictionPolicy<K, V> implements EvictionPolicy<CacheNode<K, V>> {

    private final IndexedLinkedList<CacheNode<K, V>> lruList;
    private final int maxSize;

    public LruEvictionPolicy(int maxSize) {
        // TODO: restrict maxSize > 1
        this.maxSize = maxSize;
        this.lruList = new IndexedLinkedList<>();
    }

    @Override
    public boolean shouldApply() {
        return lruList.size() > maxSize;
    }

    @Override
    public Set<CacheNode<K, V>> evictKeys() {
        Set<CacheNode<K, V>> keysEvicted = new HashSet<>();
        while (lruList.size() > maxSize) {
            keysEvicted.add(lruList.removeLast().getElement());
        }
        return keysEvicted;
    }

    @Override
    public boolean shouldEvictNode(CacheNode<K, V> cacheNode) {
        return false;
    }


    @Override
    public void resetAccess(CacheNode<K, V> cacheNode) {
        lruList.moveNodeToHead(cacheNode);
    }

    @Override
    public void resetAccess(CacheNode<K, V> cacheNode, Duration expiry) {
        resetAccess(cacheNode);
    }

}
