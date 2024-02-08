package org.cachew.cache.eviction;

import org.cachew.cache.error.CachewException;
import org.cachew.cache.internal.CacheNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class LruEvictionPolicyTest {

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
        int lruSize = 10;
        LruEvictionPolicy<String, String> policy = new LruEvictionPolicy<>(lruSize);
        Assertions.assertFalse(policy.shouldApply()); // since LRU is empty
        Assertions.assertEquals(0, policy.evictKeys().size()); // 0 keys to be evicted
        Set<CacheNode<String, String>> keysToEvict = new HashSet<>();
        int evictedIdx = 0;
        for (int i = 0; i < MAX_NODES; i++) {
            Assertions.assertFalse(policy.shouldEvictNode(ALL_NODES[i])); // always should be false;
            policy.accessKey(ALL_NODES[i]); // add to LRU
            Assertions.assertFalse(policy.shouldEvictNode(ALL_NODES[i])); // always should be false;
            Set<CacheNode<String, String>> newKeysEvicted = policy.evictKeys();

            keysToEvict.addAll(newKeysEvicted);
            if (i >= lruSize) {
                Assertions.assertEquals(1, newKeysEvicted.size()); // in each iteration one key evicted
                CacheNode<String, String> keyEvicted = newKeysEvicted.iterator().next();

                Assertions.assertEquals(ALL_NODES[evictedIdx].getKey(), keyEvicted.getKey());
                evictedIdx++;

                // Validate overall evicted
                int keysEvicted = keysToEvict.size();
                int expectedKeysToEvict = i - lruSize + 1;
                Assertions.assertEquals(expectedKeysToEvict, keysEvicted);
            }
        }
    }
}
