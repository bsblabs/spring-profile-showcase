package com.bsb.showcase.spring.profile.test;

import org.springframework.beans.factory.FactoryBean;

/**
 * Simplifies the creation of a String for testing purposes.
 *
 * @author Stephane Nicoll
 */
public class StringFactoryBean implements FactoryBean<String> {

    private String value;

    public String getObject() throws Exception {
        return value;
    }

    public Class<?> getObjectType() {
        return String.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
