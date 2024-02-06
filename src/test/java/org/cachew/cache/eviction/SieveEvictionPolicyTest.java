package org.cachew.cache.eviction;

import org.cachew.cache.error.CachewException;
import org.cachew.cache.internal.CacheNode;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class SieveEvictionPolicyTest {
    private static int MAX_NODES = 20;
    private static CacheNode<String, String>[] ALL_NODES;
    @BeforeClass
    public static void setup() {
        ALL_NODES = new CacheNode[MAX_NODES];
        for (int i = 0; i < MAX_NODES; i++) {
            ALL_NODES[i] = new CacheNode<>("test-" + i, "val-" + i);
        }
    }

    @Test
    public void testEviction() throws CachewException {
        int sieveSize = 10;
        SieveEvictionPolicy<String, String> policy = new SieveEvictionPolicy<>(sieveSize);
        Assert.assertFalse(policy.shouldApply()); // since LRU is empty
        Assert.assertEquals(0, policy.evictKeys().size()); // 0 keys to be evicted
        List<CacheNode<String, String>> keysToEvict = new LinkedList<>();
        int[] evictedOrder = new int[]{10, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11};

        for (int i = 0; i < MAX_NODES; i++) {
            Assert.assertFalse(policy.shouldEvictNode(ALL_NODES[i])); // always should be false;
            policy.accessKey(ALL_NODES[i]); // add to LRU
            Assert.assertFalse(policy.shouldEvictNode(ALL_NODES[i])); // always should be false;
            Set<CacheNode<String, String>> newKeysEvicted = policy.evictKeys();

            keysToEvict.addAll(newKeysEvicted);
            if (i >= sieveSize) {
                int evictedIdx = evictedOrder[i - sieveSize];
                Assert.assertEquals(1, newKeysEvicted.size()); // in each iteration one key evicted
                CacheNode<String, String> keyEvicted = newKeysEvicted.iterator().next();
                // first eviction will remove head since all nodes have been visited once
                //Assert.assertEquals(ALL_NODES[evictedIdx].getKey(), keyEvicted.getKey());
                evictedIdx++;

                // Validate overall evicted
                int keysEvicted = keysToEvict.size();
                int expectedKeysToEvict = i - sieveSize + 1;
                Assert.assertEquals(expectedKeysToEvict, keysEvicted);
            }

        }
        //System.out.println("Evicted order " + keysToEvict.stream().map(CacheNode::getKey).collect(Collectors.toList()));
    }
}
