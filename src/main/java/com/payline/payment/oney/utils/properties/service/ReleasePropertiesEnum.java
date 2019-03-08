package com.payline.payment.oney.utils.properties.service;

import com.payline.payment.oney.utils.properties.constants.ConfigurationConstants;
import com.payline.pmapi.logger.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

/**
 * Utility class which reads and provides config properties.
 */
public enum ReleasePropertiesEnum implements PropertiesService {


    INSTANCE;

    private final Logger logger = LogManager.getLogger(ReleasePropertiesEnum.class);

    private static final String FILENAME = ConfigurationConstants.RELEASE_PROPERTIES;

    private final Properties properties;

    /* This class has only static methods: no need to instantiate it */
    ReleasePropertiesEnum() {
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

}
