package org.cachew.cache.internal;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;

@Builder
@Data
public class CacheConfiguration {

    @Builder.Default
    private final int maxKeys = -1;

    @Builder.Default
    private final Duration globalTtlDuration = null;
}
