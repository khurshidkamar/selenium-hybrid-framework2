package com.selenium.retry;

/**
 * Retry analyzer for UI tests — allow 2 retry attempts (i.e., up to 2 retries).
 */
public class UiRetryAnalyzer extends BaseRetry {
    @Override
    protected int getMaxRetries() {
        return 2;
    }
}