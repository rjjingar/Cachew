package org.cachew.cache.eviction;

import lombok.RequiredArgsConstructor;
import org.cachew.cache.error.CachewException;
import org.cachew.cache.internal.CacheNode;
import org.cachew.cache.list.Node;
import org.cachew.cache.list.SieveLinkedList;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

/**
 * Experimental implementation. Not ready to be used.
 * @param <K>
 * @param <V>
 */
@RequiredArgsConstructor
public class SieveEvictionPolicy <K, V> implements EvictionPolicy<CacheNode<K, V>> {

    private final int maxSize;
    private final SieveLinkedList<CacheNode<K, V>> sieveList;

    public SieveEvictionPolicy(int maxSize) throws CachewException {
        if (maxSize < 2) {
            throw CachewException.LRU_INIT_ERROR;
        }
        this.maxSize = maxSize;
        this.sieveList = new SieveLinkedList<>();
    }

    @Override
    public boolean shouldApply() {
        return sieveList.size() > maxSize;
    }

    @Override
    public Set<CacheNode<K, V>> evictKeys() {
        Set<CacheNode<K, V>> keysEvicted = new HashSet<>();
        while (sieveList.size() > maxSize) {
            keysEvicted.add(sieveList.removeLast().getElement());
        }
        return keysEvicted;
    }

    @Override
    public boolean shouldEvictNode(CacheNode<K, V> cacheNode) {
        return false;
    }

    @Override
    public void accessKey(CacheNode<K, V> cacheNode) {
        Node<CacheNode<K, V>> node = sieveList.findByIndex(cacheNode);
        if (node == null) {
            node = sieveList.addFirst(cacheNode);
        }
        node.setVisited(true);
    }

    @Override
    public void accessKey(CacheNode<K, V> cacheNode, Duration ttl) {
        accessKey(cacheNode);
    }

    @Override
    public void removeKey(CacheNode<K, V> cacheNode) {

    }
}
