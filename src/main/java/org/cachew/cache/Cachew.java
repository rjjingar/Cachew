package org.cachew.cache;

import org.cachew.cache.error.CachewException;
import org.cachew.cache.error.OriginException;
import org.cachew.cache.internal.CacheConfiguration;

import java.time.Duration;

/**
 * In memory cache interface
 * @param <K> Type of Key
 * @param <V> Type of Value
 */
public interface Cachew<K, V> {

    /**
     * Clears contents of local cache
     */
    public void clean();

    /**
     * Fetches value from cache, internally applies eviction policies
     * If underlying origin is set then will refresh the cache value from origin if needed.
     * @param key cache lookup key
     * @return value for given key
     * @throws CachewException if key not found in cache or origin
     */
    public V getValue(K key) throws CachewException;

    /**
     * Puts given key, value in local cache
     * @param key cache lookup key
     * @param val value for given key
     */
    public void putValue(K key, V val);

    /**
     * Puts given key, value in local cache with provided ttl
     * @param key cache lookup key
     * @param val value for given key
     * @param ttl Duration for which this cache key is valid.
     *            Once ttl has expired cache entry will be removed.
     */
    public void putValue(K key, V val, Duration ttl);

    /**
     * Forcefully refresh cache key from origin. The value will be updated in cache as well.
     * @param key cache lookup key
     * @return value for given key
     * @throws OriginException If origin is not setup or if origin gives an error while accessing the key
     */
    public V refreshValue(K key) throws OriginException;

    /**
     * Forcefully refresh cache key from origin. The value will be updated in cache as well.
     * @param key cache lookup key
     * @param ttl expiry duration for this key. If set to null then no ttl (global ttl will not apply)
     *            if you need to use global ttl then use {@link #refreshValue(Object)}
     * @return value of the key after refreshing from origin
     * @throws OriginException If origin is not setup or if origin gives an error while accessing the key
     */
    public V refreshValue(K key, Duration ttl) throws OriginException;
}
