package org.cachew.cache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CacheStorageInMemTest {

    @Test
    public void testKeyAddRetrieve() {
        CacheStorageInMem<String, String> storage = new CacheStorageInMem<>();
        String testKey1 = "test-key-1";
        String testKey2 = "test-key-2";
        String testVal1 = "test-val-1";
        storage.setValue(testKey1, testVal1);

        Assertions.assertTrue(storage.hasKey(testKey1));
        Assertions.assertFalse(storage.hasKey(testKey2));
        Assertions.assertEquals(testVal1, storage.getCacheNode(testKey1).getValue());
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

        Assertions.assertFalse(storage.hasKey(testKey1));
        Assertions.assertNull(storage.getCacheNode(testKey1));

        Assertions.assertTrue(storage.hasKey(testKey2));
        Assertions.assertEquals(testVal2, storage.getCacheNode(testKey2).getValue());
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
        Assertions.assertFalse(storage.hasKey(testKey1));
        Assertions.assertFalse(storage.hasKey(testKey2));
    }
}
