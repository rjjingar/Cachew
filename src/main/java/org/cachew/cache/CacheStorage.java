package org.cachew.cache;

import org.cachew.cache.internal.CacheNode;

import java.time.Duration;

public interface CacheStorage<K, V> {

    public boolean hasKey(K key);


    public CacheNode<K, V> getCacheNode(K key);

    public CacheNode<K, V> setValue(K key, V value);

    public CacheNode<K, V> remove(K key);

    public void clearStorage();

}
