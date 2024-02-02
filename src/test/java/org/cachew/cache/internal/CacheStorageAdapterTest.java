package org.cachew.cache.internal;

import org.cachew.cache.CacheStorage;
import org.cachew.cache.CacheStorageInMem;
import org.cachew.cache.SourceRetriever;
import org.cachew.cache.SourceRetrieverSample;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CacheStorageAdapterTest {

    private SourceRetriever<String, String> retriever;
    private CacheStorage<String, String> storage;

    private String[] keys;
    private String[] values;

    private int maxKeys = 10;

    @Before
    public void setup() {
        Map<String, String> sampleSource = new HashMap<>();
        this.keys = new String[maxKeys];
        this.values = new String[maxKeys];
        for (int i = 0; i < maxKeys; i++) {
            keys[i] = "test-key-" + i;
            values[i] = "test-val-" + i;
            sampleSource.put(keys[i], values[i]);
        }
        this.storage = new CacheStorageInMem<>();
        this.retriever = new SourceRetrieverSample(sampleSource);
    }

    @Test
    public void testCacheUpdateFromSource() {
        CacheStorageAdapter<String, String> adapter = new CacheStorageAdapter<>(storage, retriever);

        for (int i = 0; i < maxKeys; i++) {
            Assert.assertNull(adapter.fetchFromCache(keys[i]));
            Assert.assertEquals(values[i], adapter.fetch(keys[i], null).getValue());
            Assert.assertEquals(values[i], adapter.fetchFromCache(keys[i]).getValue());
        }
    }

    @Test
    public void testPutInCache() {
        CacheStorageAdapter<String, String> adapter = new CacheStorageAdapter<>(storage, null);
        for (int i = 0; i < maxKeys; i++) {
            Assert.assertNull(adapter.fetchFromCache(keys[i]));
            adapter.putInCache(keys[i], values[i], null);

            Assert.assertEquals(values[i], adapter.fetchFromCache(keys[i]).getValue());
            Assert.assertEquals(values[i], adapter.fetch(keys[i], null).getValue());
        }
    }
}
