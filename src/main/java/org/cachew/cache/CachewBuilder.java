package org.cachew.cache;

import org.cachew.cache.eviction.EvictionPolicy;
import org.cachew.cache.internal.CacheConfiguration;
import org.cachew.cache.internal.CacheNode;
import org.cachew.cache.internal.CacheStorageAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CachewBuilder<K, V> {

    private CacheConfiguration configuration;
    private SourceRetriever<K, V> retriever;

    private CacheStorage<K, V> storage;

    private List<EvictionPolicy<CacheNode<K, V>>> evictionPolicies;

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

    public CachewBuilder<K, V> withSourceRetriever(SourceRetriever<K, V> retriever) {
        this.retriever = retriever;
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
        if (this.evictionPolicies == null) {
            this.evictionPolicies = Collections.emptyList();
        }
        final CacheStorageAdapter<K, V> adapter = new CacheStorageAdapter<>(storage, retriever);

        return new CachewImpl<>(configuration, adapter, evictionPolicies);
    }
}
