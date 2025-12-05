package com.selenium.retry;

import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.*;
import java.lang.reflect.Field;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;

/**
 * Enhanced BaseRetry:
 * - decides whether to retry
 * - on retry: marks test as flaky in Allure, attaches screenshot if driver available,
 *   logs retry attempt, and appends a CSV metric line for analysis.
 */
public abstract class BaseRetry implements IRetryAnalyzer {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private int retryCount = 0;

    protected abstract int getMaxRetries();

    @Override
    public boolean retry(ITestResult result) {
        Throwable t = result.getThrowable();

        // If max retries exhausted, do not retry
        if (retryCount >= getMaxRetries()) {
            return false;
        }

        // Decide whether the failure is retriable
        if (!isRetriable(t)) {
            log.info("Not retrying test '{}' because failure is non-retriable: {}",
                    result.getName(),
                    (t == null ? "no exception available" : t.getClass().getSimpleName()));
            return false;
        }

        retryCount++;
        log.warn("Retrying test '{}' — attempt {}/{}", result.getName(), retryCount, getMaxRetries());

        // 1) Attach an Allure step
        try {
            Allure.step("Retry attempt " + retryCount + " for test: " + result.getName());
        } catch (Exception ignore) {}

        // 2) Mark the test as flaky in Allure (so it gets the flaky label)
        try {
            Allure.label("flaky", "true");
        } catch (Exception e) {
            log.debug("Failed to set flaky label: {}", e.getMessage());
        }

        // 3) Try to capture and attach a screenshot (if WebDriver exists on test instance)
        try {
            WebDriver driver = extractWebDriverFromResult(result);
            if (driver != null) {
                try {
                    byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    Allure.getLifecycle().addAttachment("Retry Screenshot - " + result.getName() + " attempt " + retryCount,
                            "image/png", "png", bytes);
                    log.info("Attached retry screenshot for {} attempt {}", result.getName(), retryCount);
                } catch (Exception e) {
                    log.warn("Could not take screenshot for retry: {}", e.getMessage());
                }
            } else {
                log.debug("No WebDriver found for test instance; skipping retry screenshot");
            }
        } catch (Exception e) {
            log.warn("Error while attempting to attach screenshot on retry: {}", e.getMessage());
        }

        // 4) Append a CSV line for analytics
        try {
            appendRetryMetric(result, t, retryCount);
        } catch (Exception e) {
            log.warn("Failed to append retry metric: {}", e.getMessage());
        }

        return true;
    }

    /**
     * Heuristic: only retry for exceptions that are commonly transient.
     */
    protected boolean isRetriable(Throwable t) {
        if (t == null) {
            return false;
        }

        if (t instanceof NoSuchElementException
                || t instanceof StaleElementReferenceException
                || t instanceof ElementClickInterceptedException
                || (t instanceof TimeoutException)
        ) {
            return true;
        }

        if (t instanceof SocketTimeoutException || t instanceof ConnectException) {
            return true;
        }

        String msg = t.getMessage();
        if (msg != null) {
            String lower = msg.toLowerCase();
            if (lower.contains("connection refused") ||
                    lower.contains("timed out") ||
                    lower.contains("connectexception") ||
                    lower.contains("sockettimeout") ||
                    lower.contains("read timed out")) {
                return true;
            }
        }

        // Do not retry assertion errors or business logic failures
        return false;
    }

    /**
     * Try to extract WebDriver from the test instance via reflection.
     * Looks for a field named 'driver' or 'webDriver' in the test class or its superclasses.
     */
    private WebDriver extractWebDriverFromResult(ITestResult result) {
        Object testInstance = result.getInstance();
        if (testInstance == null) {
            return null;
        }

        Class<?> cls = testInstance.getClass();
        while (cls != Object.class) {
            for (Field f : cls.getDeclaredFields()) {
                String name = f.getName().toLowerCase();
                if (name.equals("driver") || name.equals("webdriver")) {
                    try {
                        f.setAccessible(true);
                        Object value = f.get(testInstance);
                        if (value instanceof WebDriver) {
                            return (WebDriver) value;
                        }
                    } catch (IllegalAccessException e) {
                        log.debug("Could not access driver field: {}", e.getMessage());
                    }
                }
            }
            cls = cls.getSuperclass();
        }
        return null;
    }

    /**
     * Append a CSV line containing:
     * timestamp, testClass, testMethod, throwableClass, throwableMessage (short), attemptNumber
     * File: retries/retry-metrics.csv (creates header if missing)
     */
    private void appendRetryMetric(ITestResult result, Throwable t, int attemptNumber) {
        try {
            File folder = new File("retries");
            if (!folder.exists()) {
                folder.mkdirs();
            }

            File csv = new File(folder, "retry-metrics.csv");
            boolean needHeader = !csv.exists();

            String ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String testClass = result.getTestClass() == null ? "?" : result.getTestClass().getName();
            String testMethod = result.getMethod() == null ? result.getName() : result.getMethod().getMethodName();
            String exc = t == null ? "" : t.getClass().getSimpleName();
            String msg = t == null ? "" : sanitizeForCsv(t.getMessage());

            String line = String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%d%n", ts, testClass, testMethod, exc, msg, attemptNumber);

            if (needHeader) {
                String header = "\"timestamp\",\"testClass\",\"testMethod\",\"exception\",\"message\",\"attempt\"%n";
                Files.write(csv.toPath(), String.format(header).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            }
            Files.write(csv.toPath(), line.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            log.warn("Failed to write retry metric CSV: {}", e.getMessage());
        }
    }

    private String sanitizeForCsv(String s) {
        if (s == null) return "";
        String singleLine = s.replaceAll("[\\r\\n]+", " ");
        singleLine = singleLine.replace("\"", "'");
        if (singleLine.length() > 300) {
            return singleLine.substring(0, 300);
        }
        return singleLine;
    }
}
