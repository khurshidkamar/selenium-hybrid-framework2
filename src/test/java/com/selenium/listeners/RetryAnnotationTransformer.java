package com.selenium.listeners;

import com.selenium.retry.ApiRetryAnalyzer;
import com.selenium.retry.UiRetryAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Automatically assigns a RetryAnalyzer to tests:
 * - If the test class package contains ".api.", assign ApiRetryAnalyzer
 * - Otherwise assign UiRetryAnalyzer (default)
 *
 * This avoids putting retry annotations on each test.
 */
public class RetryAnnotationTransformer implements IAnnotationTransformer {

    private final Logger log = LoggerFactory.getLogger(RetryAnnotationTransformer.class);

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        try {
            // if testClass is in API package, apply ApiRetryAnalyzer
            if (testClass != null && testClass.getName().contains(".api.")) {
                annotation.setRetryAnalyzer(ApiRetryAnalyzer.class);
                log.debug("Assigned ApiRetryAnalyzer to {}#{}", testClass.getName(),
                        testMethod == null ? "?" : testMethod.getName());
                return;
            }

            // If the test method explicitly belongs to 'api' group, prefer ApiRetryAnalyzer
            if (testMethod != null) {
                String[] groups = annotation.getGroups();
                if (groups != null) {
                    for (String g : groups) {
                        if ("api".equalsIgnoreCase(g.trim())) {
                            annotation.setRetryAnalyzer(ApiRetryAnalyzer.class);
                            log.debug("Assigned ApiRetryAnalyzer to {}#{} by group", testClass.getName(), testMethod.getName());
                            return;
                        }
                    }
                }
            }

            // default: UI retry analyzer
            annotation.setRetryAnalyzer(UiRetryAnalyzer.class);
            log.debug("Assigned UiRetryAnalyzer to {}#{}", testClass == null ? "?" : testClass.getName(),
                    testMethod == null ? "?" : testMethod.getName());

        } catch (Exception e) {
            log.error("Error while assigning retry analyzer", e);
        }
    }
}
