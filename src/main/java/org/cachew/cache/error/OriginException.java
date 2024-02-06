package org.cachew.cache.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class OriginException extends Exception {

    @Getter
    @RequiredArgsConstructor
    public enum OriginErrorCode {
        ORIGIN_NOT_SET("Cache origin not set", false),
        KEY_NOT_FOUND_IN_ORIGIN("Key not found in origin ", false);
        private final String messagePrefix;
        private final boolean shouldRetry;
    }

    private final OriginErrorCode errorCode;

    public OriginException(final OriginErrorCode errorCode) {
        this(errorCode, "", null);
    }

    public OriginException(final OriginErrorCode errorCode, String messageSuffix) {
        this(errorCode, messageSuffix, null);
    }

    public OriginException(final OriginErrorCode errorCode, String messageSuffix, Throwable e) {
        super(errorCode.getMessagePrefix() + messageSuffix, e);
        this.errorCode = errorCode;
    }
}
