//package com.payline.payment.oney.utils.properties.service;
//
//import com.payline.payment.oney.utils.config.ConfigEnvironment;
//
//import java.util.Locale;
//import java.util.Properties;
//
///**
// * Utility class which reads and provides config properties.
// */
//public class ConfigProperties implements PropertiesService {
//
//    private static final String FILENAME = "config.properties";
//
//    private static Properties properties;
//
//    /* This class has only static methods: no need to instantiate it */
//    private ConfigProperties() {
//        properties = new Properties();
//        // init of the Properties
//        readProperties(properties);
//    }
//
//    /**
//     * Holder
//     */
//    private static class SingletonHolder {
//        /**
//         * Unique instance, not preinitializes
//         */
//        private static final ConfigProperties instance = new ConfigProperties();
//    }
//
//    /**
//     * Unique access point for the singleton instance
//     */
//    public static ConfigProperties getInstance() {
//        return ConfigProperties.SingletonHolder.instance;
//    }
//
//    /**
//     * Get a config property by its name.
//     * Warning, if the property is environment-dependent, use {@link ConfigProperties#get(String, ConfigEnvironment)} instead.
//     *
//     * @param key The name of the property to recover
//     * @return The property value. Can be null if the property has not been found.
//     */
//    public static String get(String key) {
//
//        return properties.getProperty(key);
//    }
//
//    /**
//     * Get a environment-dependent config property by its name.
//     *
//     * @param key         The name of the property to recover
//     * @param environment The runtime environment
//     * @return The property value. Can be null if the property has not been found.
//     */
//    public static String get(String key, ConfigEnvironment environment) {
//        String prefix = "";
//        if (environment != null) {
//            prefix += environment.getPrefix() + ".";
//        }
//        return get(prefix + key);
//    }
//
//
//    @Override
//    public String getMessage(String key, Locale locale) {
//        return null;
//    }
//
//    @Override
//    public String getFilename() {
//        return FILENAME;
//    }
//}
