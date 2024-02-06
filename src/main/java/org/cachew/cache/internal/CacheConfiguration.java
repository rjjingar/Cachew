package org.cachew.cache.internal;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;

/**
 * Global configuration for the {@link org.cachew.cache.Cachew} implementation
 */
@Builder
@Data
public class CacheConfiguration {

    /**
     * Max keys that can be allocated in this cache. Default is -1
     * -1 means there is no limit.
     */
    @Builder.Default
    private final int maxKeys = -1;

    /**
     * Optional setting to specify a global default ttl
     * if null then no ttl is applied by default
     * additionally individual keys can have their own ttl at the time of insertion
     */
    @Builder.Default
    private final Duration globalTtlDuration = null;
}
