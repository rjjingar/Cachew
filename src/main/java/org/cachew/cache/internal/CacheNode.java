package org.cachew.cache.internal;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@Data
public class CacheNode<K, V> {

    private final K key;
    private V value;
    private long expiryTime;
    private long lastAccessedTime;

    public CacheNode(@NonNull K key, V value) {
        this.key = key;
        this.value = value;
        this.lastAccessedTime = System.currentTimeMillis();
        this.expiryTime = -1;
    }

    public CacheNode(@NonNull final K key, final V value, final Duration ttlDuration) {
        long currTime = System.currentTimeMillis();
        this.key = key;
        this.value = value;
        this.lastAccessedTime = currTime;
        this.expiryTime = ttlDuration == null ? -1 : currTime + ttlDuration.toMillis();
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
        this.resetLastAccessTime();
    }

    public void resetLastAccessTime() {
        this.lastAccessedTime = System.currentTimeMillis();
    }
}
