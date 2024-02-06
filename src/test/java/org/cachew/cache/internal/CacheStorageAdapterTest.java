package org.cachew.cache.internal;

import org.cachew.cache.CacheStorage;
import org.cachew.cache.CacheStorageInMem;
import org.cachew.cache.CacheOrigin;
import org.cachew.cache.SourceRetrieverSample;
import org.cachew.cache.error.CachewException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CacheStorageAdapterTest {

    private CacheOrigin<String, String> origin;
    private CacheStorage<String, String> cache;

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
        this.cache = new CacheStorageInMem<>();
        this.origin = new SourceRetrieverSample(sampleSource);
    }

    @Test
    public void testCacheUpdateFromSource() throws CachewException {
        CacheStorageAdapter<String, String> adapter = new CacheStorageAdapter<>(cache, origin);

        for (int i = 0; i < maxKeys; i++) {
            Assert.assertNull(adapter.fetchFromCache(keys[i]));
            Assert.assertEquals(values[i], adapter.fetch(keys[i], null).getValue());
            Assert.assertEquals(values[i], adapter.fetchFromCache(keys[i]).getValue());
        }
    }

    @Test
    public void testKeyNotFoundInOrigin() {
        CacheStorageAdapter<String, String> adapter = new CacheStorageAdapter<>(cache, origin);
        boolean exceptionFound = false;
        try {
            adapter.fetch("invalid-key", null);
        } catch (CachewException e) {
            exceptionFound = true;
            Assert.assertEquals(CachewException.CachewErrorCode.KEY_NOT_FOUND_ORIGIN, e.getErrorCode());
        }
        Assert.assertTrue(exceptionFound);
    }

    @Test
    public void testKeyNotFoundInCache() {
        CacheStorageAdapter<String, String> adapter = new CacheStorageAdapter<>(cache, null); // no origin
        boolean exceptionFound = false;
        try {
            adapter.fetch("invalid-key", null);
        } catch (CachewException e) {
            exceptionFound = true;
            Assert.assertEquals(CachewException.CachewErrorCode.KEY_NOT_FOUND_CACHE, e.getErrorCode());
        }
        Assert.assertTrue(exceptionFound);
    }

    @Test
    public void testPutInCache() throws CachewException {
        CacheStorageAdapter<String, String> adapter = new CacheStorageAdapter<>(cache, null);
        for (int i = 0; i < maxKeys; i++) {
            Assert.assertNull(adapter.fetchFromCache(keys[i]));
            adapter.putInCache(keys[i], values[i], null);

            Assert.assertEquals(values[i], adapter.fetchFromCache(keys[i]).getValue());
            Assert.assertEquals(values[i], adapter.fetch(keys[i], null).getValue());
        }
    }
}
