package org.cachew.cache;

import org.cachew.cache.internal.CacheConfiguration;

import java.time.Duration;

public interface Cachew<K, V> {

    public void clean();

    public V getValue(K key);

    public void putValue(K key, V val);

    public void putValue(K key, V val, Duration ttl);

    public V refreshValue(K key);
}
