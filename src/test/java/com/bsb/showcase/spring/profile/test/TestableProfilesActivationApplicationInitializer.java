package com.bsb.showcase.spring.profile.test;


import com.bsb.showcase.spring.profile.ProfilesActivationApplicationInitializer;

/**
 * A simple extension of {@link ProfilesActivationApplicationInitializer} used
 * for testing purposes
 *
 * @author Stephane Nicoll
 */
public class TestableProfilesActivationApplicationInitializer extends ProfilesActivationApplicationInitializer {

    private String propertiesPath = PROPERTIES_LOCATION;

    @Override
    protected String getPropertiesPath() {
        return propertiesPath;
    }

    /**
     * Sets the location to properties file to use.
     *
     * @param propertiesPath the properties path
     */
    public void setPropertiesPath(String propertiesPath) {
        this.propertiesPath = propertiesPath;
    }

}
