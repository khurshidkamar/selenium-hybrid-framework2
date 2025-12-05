package com.selenium.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        loadConfig();
    }

    private static void loadConfig() {
        try {
            // Load default config
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            properties.load(fis);

            // Check if environment override exists
            String env = System.getProperty("environment", properties.getProperty("environment"));

            String envFilePath = "src/test/resources/config-" + env + ".properties";

            FileInputStream envFis = new FileInputStream(envFilePath);
            properties.load(envFis);

            System.out.println("Loaded environment: " + env);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config files", e);
        }
    }

    // 🔧 Getters
    public static String getBrowser() {
        return System.getProperty("browser", properties.getProperty("browser"));
    }
    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }
    public static String getBaseUrl1() {
        return properties.getProperty("base.url1");
    }

    public static String getApiBaseUrl() {
        return properties.getProperty("api.base.url");
    }

    public static int getImplicitWait() {
        return Integer.parseInt(properties.getProperty("implicit.wait", "10"));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(properties.getProperty("page.load.timeout", "20"));
    }
}
