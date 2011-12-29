package com.bsb.showcase.spring.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * An {@link ApplicationContextInitializer} loading the profiles to activate from
 * a properties file on the classpath, loaded from the {@link #getPropertiesPath}
 * <p/>
 * Does nothing if the properties file does not exist. Added last so that overriding
 * through system property still works.
 * <p/>
 * Subclass may override the {@link #createPropertySource()} to create a completely
 * different {@link PropertySource} instance, if needed.
 *
 * @author Stephane Nicoll
 */
public class ProfilesActivationApplicationInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    /**
     * The default location to use.
     */
    public static final String PROPERTIES_LOCATION = "META-INF/application-context.properties";

    private final Logger logger = LoggerFactory.getLogger(ProfilesActivationApplicationInitializer.class);

    public void initialize(ConfigurableApplicationContext applicationContext) {
        final PropertySource ps = createPropertySource();
        applicationContext.getEnvironment().getPropertySources().addLast(ps);
    }

    /**
     * Creates the {@link PropertySource} to use
     *
     * @return the property source to use
     */
    protected PropertySource createPropertySource() {
        return new PropertiesPropertySource("application-context-bootstrap", loadProperties(getPropertiesPath()));
    }

    /**
     * Returns the location of the properties file in the classpath.
     *
     * @return the location to use to load the properties file
     * @see #PROPERTIES_LOCATION
     */
    protected String getPropertiesPath() {
        return PROPERTIES_LOCATION;
    }

    /**
     * Loads a properties from the specified location in the classpath. Returns
     * an empty {@link java.util.Properties} instance if no such file was found.
     *
     * @param path the path of the properties file on the classpath
     * @return the properties file at the specified location or an empty properties instance if not found
     */
    private Properties loadProperties(String path) {
        if (logger.isDebugEnabled()) {
            logger.debug("Loading application context bootstrap properties from [" + path + "]");
        }
        final Resource resource = new ClassPathResource(path);
        try {
            final Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            logFoundProfiles(properties);
            return properties;
        } catch (IOException e) {
            logger.info("No application context bootstrap properties found in [" + path + "]");
            return new Properties();
        }
    }

    private void logFoundProfiles(Properties properties) {
        final String profiles = properties.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
        if (profiles != null) {
            logger.debug("Found user-defined profiles [" + profiles + "]");
        } else {
            logger.info("Application context bootstrap properties does not define any profile to activate. Set " +
                    "the [" + AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME + "] property to enable it.");
        }
    }
}
