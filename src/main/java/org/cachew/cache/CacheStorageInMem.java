package org.cachew.cache;

import lombok.NonNull;
import org.cachew.cache.internal.CacheNode;

import java.util.concurrent.ConcurrentHashMap;

public class CacheStorageInMem<K, V> implements CacheStorage<K, V> {

    private final ConcurrentHashMap<K, CacheNode<K, V>> map;

    public CacheStorageInMem() {
        this.map = new ConcurrentHashMap<>();
    }

    @Override
    public boolean hasKey(K key) {
        return map.containsKey(key);
    }

    @Override
    public CacheNode<K, V> getCacheNode(final K key) {
        return map.getOrDefault(key, null);
    }

    @Override
    public CacheNode<K, V> setValue(@NonNull K key, V value) {
        CacheNode<K, V> cacheNode = getCacheNode(key);
        if (cacheNode == null) {
            cacheNode = new CacheNode<>(key, value);
        } else {
            cacheNode.setValue(value);
        }
        map.put(key, cacheNode);
        return cacheNode;
    }

    @Override
    public CacheNode<K, V> remove(K key) {
        if (!hasKey(key)) {
            return null;
        }
        return map.remove(key);
    }

    @Override
    public void clearStorage() {
        this.map.clear();
    }

    @Override
    public int size() {
        return map.size();
    }
}
