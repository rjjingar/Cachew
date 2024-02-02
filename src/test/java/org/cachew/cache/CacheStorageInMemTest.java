package org.cachew.cache;

import org.cachew.cache.CacheStorageInMem;
import org.junit.Assert;
import org.junit.Test;

public class CacheStorageInMemTest {

    @Test
    public void testKeyAddRetrieve() {
        CacheStorageInMem<String, String> storage = new CacheStorageInMem<>();
        String testKey1 = "test-key-1";
        String testKey2 = "test-key-2";
        String testVal1 = "test-val-1";
        storage.setValue(testKey1, testVal1);

        Assert.assertTrue(storage.hasKey(testKey1));
        Assert.assertFalse(storage.hasKey(testKey2));
        Assert.assertEquals(testVal1, storage.getCacheNode(testKey1).getValue());
    }

    @Test
    public void testKeyAddRemove() {
        CacheStorageInMem<String, String> storage = new CacheStorageInMem<>();
        String testKey1 = "test-key-1";
        String testKey2 = "test-key-2";
        String testVal1 = "test-val-1";
        String testVal2 = "test-val-2";
        storage.setValue(testKey1, testVal1);
        storage.setValue(testKey2, testVal2);

        storage.remove(testKey1);

        Assert.assertFalse(storage.hasKey(testKey1));
        Assert.assertNull(storage.getCacheNode(testKey1));

        Assert.assertTrue(storage.hasKey(testKey2));
        Assert.assertEquals(testVal2, storage.getCacheNode(testKey2).getValue());
    }

    @Test
    public void testClear() {
        CacheStorageInMem<String, String> storage = new CacheStorageInMem<>();
        String testKey1 = "test-key-1";
        String testKey2 = "test-key-2";
        String testVal1 = "test-val-1";
        String testVal2 = "test-val-2";
        storage.setValue(testKey1, testVal1);
        storage.setValue(testKey2, testVal2);

        storage.clearStorage();
        Assert.assertFalse(storage.hasKey(testKey1));
        Assert.assertFalse(storage.hasKey(testKey2));
    }
}
