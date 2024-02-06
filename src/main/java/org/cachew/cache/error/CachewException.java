package org.cachew.cache.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class CachewException extends Exception {

    public static final CachewException LRU_INIT_ERROR =
            new CachewException(CachewErrorCode.CACHE_INIT_ERROR, "Eviction policy should have max size > 1");

    @Getter
    @RequiredArgsConstructor
    public enum CachewErrorCode {
        CACHE_INIT_ERROR("Cache initialization error ", false),
        KEY_NOT_FOUND_CACHE("Key not found in cache ", false),
        KEY_NOT_FOUND_ORIGIN("Key not found in cache or origin ", true),
        CACHE_FULL("Cache is full, can't add new keys. " +
                "Consider increasing size or apply eviction policy", false);

        private final String messagePrefix;
        private final boolean shouldRetry;
    }

    private final CachewErrorCode errorCode;

    public CachewException(final CachewErrorCode errorCode) {
        this(errorCode, "", null);
    }
    public CachewException(final CachewErrorCode errorCode, final String message) {
        this(errorCode, message, null);
    }


    public CachewException(final CachewErrorCode errorCode, final String message, final Throwable e) {
        super(errorCode.getMessagePrefix() + message, e);
        this.errorCode = errorCode;
    }
}
