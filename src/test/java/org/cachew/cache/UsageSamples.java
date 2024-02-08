package org.cachew.cache;

import org.cachew.cache.error.CachewException;
import org.cachew.cache.error.OriginException;
import org.cachew.cache.internal.CacheConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UsageSamples {

    @Test
    public void simpleUsage() throws CachewException {
        Cachew<String, String> cachew = new CachewBuilder<String, String>().build();
        cachew.putValue("key-1", "val-1");
        String value = cachew.getValue("key-1");
        Assertions.assertEquals("val-1", value);
    }

    //@Test (commented for faster build)
    public void ttlUsage() throws CachewException, InterruptedException {
        // Using ttl
        CacheConfiguration config = CacheConfiguration.builder()
                .globalTtlDuration(Duration.ofSeconds(10)) // Optionally (but recommended) specify default ttl for each key inserted into cache
                                                          // ttl can be also be specified for each key
                .build();

        Cachew<String, String> cachew = new CachewBuilder<String, String>()
                .withCacheConfiguration(config)
                .build();

        cachew.putValue("key-1", "val-1"); // Default ttl of 10 seconds will apply
        cachew.putValue("key-2", "val-2", Duration.ofSeconds(1)); // custom ttl of 1 seconds will apply

        // Both keys will exist
        Assertions.assertEquals("val-1", cachew.getValue("key-1"));
        Assertions.assertEquals("val-2", cachew.getValue("key-2"));

        Thread.sleep(1000); // wait till ttl of key-2
        Assertions.assertEquals("val-1", cachew.getValue("key-1")); // key-1 still in cache since its ttl has not expired yet.
        // key-2 should have expired
        CachewException keyError2 = Assertions.assertThrows(CachewException.class, () -> cachew.getValue("key-2"));
        Assertions.assertEquals(CachewException.CachewErrorCode.KEY_NOT_FOUND_CACHE, keyError2.getErrorCode());

        Thread.sleep(9000); // wait till ttl of key-1
        // key-1 should have expired
        CachewException keyError1 = Assertions.assertThrows(CachewException.class, () -> cachew.getValue("key-1"));
        Assertions.assertEquals(CachewException.CachewErrorCode.KEY_NOT_FOUND_CACHE, keyError1.getErrorCode());
    }

    @Test
    public void LruUsage() throws CachewException {
        // Using LRU eviction policy
        CacheConfiguration config = CacheConfiguration.builder()
                .maxKeys(100) // defaults to -1 for no limit. If a limit is specified then by default LRU cache eviction will apply
                .build();

        Cachew<String, String> cachew = new CachewBuilder<String, String>()
                .withCacheConfiguration(config)
                .build();

        for (int i = 1; i <= 100; i++) {
            // key-1 to key-100
            cachew.putValue("key-" + i, "val-" + i);
        }
        for (int i = 1; i <= 100; i++) {
            // All keys should be present since cache has not breached its max size
            Assertions.assertEquals("val-" + i, cachew.getValue("key-" + i));
        }
        for (int i = 101; i <= 200; i++) {
            // key-101 to key-200
            cachew.putValue("key-" + i, "val-" + i);
            // key-1 to key-100 will get evicted sequentially
            String evictedKey = "key-" + (i - 100);
            CachewException evictedKeyError = Assertions.assertThrows(CachewException.class, () -> cachew.getValue(evictedKey));
            Assertions.assertEquals(CachewException.CachewErrorCode.KEY_NOT_FOUND_CACHE, evictedKeyError.getErrorCode());
        }
    }

    @Test
    public void withCacheOrigin() throws CachewException {
        Cachew<String, String> cachew = new CachewBuilder<String, String>()
                .withCacheOrigin(new MyCacheOrigin())
                .build();
        for (int i = 1; i <= 100; i++) {
            // All keys should be present since origin has keys from key-1 to key-100
            Assertions.assertEquals("val-" + i, cachew.getValue("key-" + i));
        }
        // key-101 is not in origin
        CachewException missingKeyError = Assertions.assertThrows(CachewException.class, () -> cachew.getValue("key-101"));
        Assertions.assertEquals(CachewException.CachewErrorCode.KEY_NOT_FOUND_ORIGIN, missingKeyError.getErrorCode());
    }


    public static class MyCacheOrigin implements CacheOrigin<String, String> {
        private final Map<String, String> storageMap;

        public MyCacheOrigin() {
            this.storageMap = new HashMap<>();
            for (int i = 1; i <= 100; i++) {
                this.storageMap.put("key-" + i, "val-" + i);
            }
        }
        @Override
        public Optional<String> retrieveValue(String key) throws OriginException {
            // call some remote service or fetch from a database.
            // in this example we are fetching values from a hash map.
            if (!storageMap.containsKey(key)) {
                throw new OriginException(OriginException.OriginErrorCode.KEY_NOT_FOUND_IN_ORIGIN);
            }
            return Optional.of(storageMap.get(key));
        }
    }
}
