package com.selenium.retry;

/**
 * Retry analyzer for API tests — allow 1 retry attempt.
 */
public class ApiRetryAnalyzer extends BaseRetry {
    @Override
    protected int getMaxRetries() {
        return 1;
    }
}