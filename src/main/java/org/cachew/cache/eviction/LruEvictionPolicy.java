package org.cachew.cache.eviction;

import org.cachew.cache.error.CachewException;
import org.cachew.cache.internal.CacheNode;
import org.cachew.cache.list.IndexedLinkedList;
import org.cachew.cache.list.Node;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class LruEvictionPolicy<K, V> implements EvictionPolicy<CacheNode<K, V>> {


    private final IndexedLinkedList<CacheNode<K, V>> lruList;
    private final int maxSize;

    public LruEvictionPolicy(int maxSize) throws CachewException {
        if (maxSize < 2) {
            throw CachewException.LRU_INIT_ERROR;
        }
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
    public void accessKey(CacheNode<K, V> cacheNode) {
        lruList.moveNodeToHead(cacheNode);
    }

    @Override
    public void accessKey(CacheNode<K, V> cacheNode, Duration expiry) {
        accessKey(cacheNode);
    }

    @Override
    public void removeKey(CacheNode<K, V> cacheNode) {
        Node<CacheNode<K, V>> curr = lruList.findByIndex(cacheNode);
        if (curr != null) {
            lruList.removeNode(curr);
        }
    }

}
