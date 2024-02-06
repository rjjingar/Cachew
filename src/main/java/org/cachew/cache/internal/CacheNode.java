package org.cachew.cache.internal;

import lombok.Data;
import lombok.NonNull;

import java.time.Duration;
import java.util.Objects;

@Data
public class CacheNode<K, V> {

    private final K key;
    private V value;
    private long expiryTime;
    private long lastAccessedTime;
    private long accessedCount;

    public CacheNode(@NonNull K key, V value) {
        this.key = key;
        this.value = value;
        this.lastAccessedTime = System.currentTimeMillis();
        this.expiryTime = -1;
        this.accessedCount = 0;
    }

    public CacheNode(@NonNull final K key, final V value, final Duration ttlDuration) {
        long currTime = System.currentTimeMillis();
        this.key = key;
        this.value = value;
        this.lastAccessedTime = currTime;
        this.expiryTime = ttlDuration == null ? -1 : currTime + ttlDuration.toMillis();
        this.accessedCount = 0;
    }

    public long setExpiry(final Duration duration) {
        if (duration != null) {
            this.expiryTime = System.currentTimeMillis() + duration.toMillis();
        }
        return this.expiryTime;
    }

    public boolean hasExpired() {
        long currTime = System.currentTimeMillis();
        return this.expiryTime > 0 && currTime >= expiryTime;
    }

    public void setValue(final V value) {
        this.value = value;
        this.resetAccessTime();
    }

    public void resetAccessTime() {
        this.lastAccessedTime = System.currentTimeMillis();
        this.accessedCount++;
    }

    public void incrementAccessCount() {
        this.accessedCount++;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof CacheNode) {
            CacheNode otherCacheNode = (CacheNode) other;
            return Objects.equals(this.getKey(), otherCacheNode.getKey());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getKey());
    }
}
