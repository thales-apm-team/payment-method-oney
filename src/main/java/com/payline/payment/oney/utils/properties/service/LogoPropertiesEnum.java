package com.payline.payment.oney.utils.properties.service;

import com.payline.payment.oney.utils.config.ConfigEnvironment;
import com.payline.payment.oney.utils.properties.constants.LogoConstants;
import com.payline.pmapi.logger.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

/**
 * Utility class which reads and provides config properties.
 */
public enum LogoPropertiesEnum implements PropertiesService {

    INSTANCE;

    private final Logger logger = LogManager.getLogger(LogoPropertiesEnum.class);

    private static final String FILENAME = LogoConstants.LOGO_PROPERTIES;

    private final Properties properties;

    /* This class has only static methods: no need to instantiate it */
    LogoPropertiesEnum() {
        properties = new Properties();
        // init of the Properties
        readProperties(properties, logger);
    }


    @Override
    public String getFilename() {
        return FILENAME;
    }

    @Override
    public String get(String key) {
        return getProperty(properties, key);
    }

    @Override
    public String get(String key, ConfigEnvironment environment) {
        return getProperty(properties, key, environment);
    }
}
