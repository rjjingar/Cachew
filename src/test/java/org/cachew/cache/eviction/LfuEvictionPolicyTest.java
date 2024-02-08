package org.cachew.cache.eviction;

import org.cachew.cache.error.CachewException;
import org.cachew.cache.internal.CacheNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class LfuEvictionPolicyTest {

    private static int MAX_NODES = 20;
    private static CacheNode<String, String>[] ALL_NODES;
    @BeforeAll
    public static void setup() {
        ALL_NODES = new CacheNode[MAX_NODES];
        for (int i = 0; i < MAX_NODES; i++) {
            ALL_NODES[i] = new CacheNode<>("test-" + i, "val-" + i);
        }
    }

    @Test
    public void testEviction() throws CachewException {
        int cacheSize = 3;
        LfuEvictionPolicy<String, String> policy = new LfuEvictionPolicy<>(cacheSize);

        Assertions.assertFalse(policy.shouldApply()); // since LRU is empty
        Assertions.assertEquals(0, policy.evictKeys().size()); // 0 keys to be evicted


        for (int i = 0; i < 10; i++) policy.accessKey(ALL_NODES[0]);
        for (int i = 0; i < 5; i++) policy.accessKey(ALL_NODES[1]); // least frequent accessed
        for (int i = 0; i < 20; i++) policy.accessKey(ALL_NODES[2]);
        for (int i = 0; i < 20; i++) policy.accessKey(ALL_NODES[3]);


        Assertions.assertEquals(true, policy.shouldApply());
        Set<CacheNode<String, String>> newKeysEvicted = policy.evictKeys();
        Assertions.assertEquals(1, newKeysEvicted.size());
        Assertions.assertEquals(ALL_NODES[1], newKeysEvicted.iterator().next());
    }
}
