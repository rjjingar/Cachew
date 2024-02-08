package org.cachew.cache;

import org.cachew.cache.error.CachewException;
import org.cachew.cache.eviction.EvictionPolicy;
import org.cachew.cache.eviction.LruEvictionPolicy;
import org.cachew.cache.internal.CacheConfiguration;
import org.cachew.cache.internal.CacheNode;
import org.cachew.cache.internal.CacheStorageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * This builds the in memory local cache with provided configurations and implementations
 * @param <K> Type of Key
 * @param <V> Type of Value
 */
public class CachewBuilder<K, V> {

    private CacheConfiguration configuration;
    private CacheOrigin<K, V> cacheOrigin;
    private CacheStorage<K, V> storage;

    private List<EvictionPolicy<CacheNode<K, V>>> evictionPolicies;

    /**
     * Specifies {@link CacheConfiguration} to be used in this cache.
     * @param configuration
     * @return
     */
    public CachewBuilder<K, V> withCacheConfiguration(CacheConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    public CachewBuilder<K, V> withEvictionPolicy(EvictionPolicy<CacheNode<K, V>> policy) {
        if (this.evictionPolicies == null) {
            this.evictionPolicies = new ArrayList<>();
        }

        this.evictionPolicies.add(policy);
        return this;
    }

    public CachewBuilder<K, V> withCacheOrigin(CacheOrigin<K, V> origin) {
        this.cacheOrigin = origin;
        return this;
    }

    public CachewBuilder<K, V> withStorage(CacheStorage<K, V> storage) {
        this.storage = storage;
        return this;
    }

    public Cachew<K, V> build() {
        if (this.storage == null) {
            this.storage = new CacheStorageInMem<>();
        }
        if (this.configuration == null) {
            this.configuration = CacheConfiguration.builder().build();
        }
        setDefaultEvictionPolicyIfNotSet();
        final CacheStorageAdapter<K, V> adapter = new CacheStorageAdapter<>(storage, cacheOrigin);

        return new CachewImpl<>(configuration, adapter, evictionPolicies);
    }

    private void setDefaultEvictionPolicyIfNotSet() {
        if (this.evictionPolicies != null && !this.evictionPolicies.isEmpty()) {
            return;
        }

        this.evictionPolicies = new ArrayList<>();

        if (this.configuration != null) {
            if (this.configuration.getMaxKeys() > 0) {
                try {
                    this.evictionPolicies.add(new LruEvictionPolicy<>(this.configuration.getMaxKeys()));
                } catch (CachewException e) {}
            }
        }
    }
}
