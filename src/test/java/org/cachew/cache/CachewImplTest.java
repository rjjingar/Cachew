package org.cachew.cache;

import org.cachew.cache.error.CachewException;
import org.cachew.cache.error.OriginException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class CachewImplTest {

    /** Default cachew */
    @Test
    public void testDefaults() throws CachewException {
        Cachew<String, String> cachew = new CachewBuilder<String, String>().build();
        cachew.putValue("key-1", "val-1");
        Assertions.assertEquals("val-1", cachew.getValue("key-1"));
        cachew.clean();
        CachewException exception = Assertions.assertThrows(CachewException.class, () -> cachew.getValue("key-1"));
        Assertions.assertEquals(CachewException.CachewErrorCode.KEY_NOT_FOUND_CACHE, exception.getErrorCode());
    }

    @Test
    public void testDefaultMissingKey() {
        Cachew<String, String> cachew = new CachewBuilder<String, String>().build();
        CachewException exception = Assertions.assertThrows(CachewException.class, () -> cachew.getValue("invalid"));
        Assertions.assertEquals(CachewException.CachewErrorCode.KEY_NOT_FOUND_CACHE, exception.getErrorCode());
    }

    @Test
    public void testDefaultOriginNotSet() {
        Cachew<String, String> cachew = new CachewBuilder<String, String>().build();
        cachew.putValue("key-1", "val-1");
        OriginException exception = Assertions.assertThrows(OriginException.class, () -> cachew.refreshValue("key-1"));
        Assertions.assertEquals(OriginException.OriginErrorCode.ORIGIN_NOT_SET, exception.getErrorCode());
    }

    /** With a cache origin */

    @Test
    public void testWithCacheOrigin() throws OriginException, CachewException {
        CacheOrigin<String, String> origin = createCacheOrigin(10);
        Cachew<String, String> cachew = new CachewBuilder<String, String>().withCacheOrigin(origin).build();
        for (int i = 0; i < 10; i++) {
            String key = "test-key-" + i;
            String expectedVal = "test-val-" + i;
            Assertions.assertEquals(expectedVal, cachew.getValue(key));
        }
    }

    @Test
    public void testWithCacheOriginMissingKey() throws OriginException, CachewException {
        CacheOrigin<String, String> origin = createCacheOrigin(10);
        Cachew<String, String> cachew = new CachewBuilder<String, String>().withCacheOrigin(origin).build();
        String missingKey = "missing-key";
        OriginException originError = Assertions.assertThrows(OriginException.class, () -> cachew.refreshValue(missingKey));
        Assertions.assertEquals(OriginException.OriginErrorCode.KEY_NOT_FOUND_IN_ORIGIN, originError.getErrorCode());

        CachewException cachewError = Assertions.assertThrows(CachewException.class, () -> cachew.getValue(missingKey));
        Assertions.assertEquals(CachewException.CachewErrorCode.KEY_NOT_FOUND_ORIGIN, cachewError.getErrorCode());
    }


    private static CacheOrigin<String, String> createCacheOrigin(int maxKeys) {
        Map<String, String> sampleSource = new HashMap<>();
        String[] keys = new String[maxKeys];
        String[] values = new String[maxKeys];
        for (int i = 0; i < maxKeys; i++) {
            keys[i] = "test-key-" + i;
            values[i] = "test-val-" + i;
            sampleSource.put(keys[i], values[i]);
        }
        return new CacheOriginSample(sampleSource);
    }
}
