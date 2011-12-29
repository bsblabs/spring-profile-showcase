package com.bsb.showcase.spring.profile;

import com.bsb.showcase.spring.profile.test.TestableProfilesActivationApplicationInitializer;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static junit.framework.Assert.assertEquals;

public class ProfilesActivationApplicationInitializerTest {

    public static final String SIMPLE_PROFILES_CONTEXT =
            "classpath:/context/simple-profiles-context.xml";
    public static final String OVERRIDING_PROFILES_CONTEXT =
            "classpath:/context/overriding-profiles-context.xml";

    public static final String DUMMY_PROPERTIES_PATH =
            "classpath:/test/no/chance/to/be/there/my.properties";


    private static final String NO_PROFILE_BEAN_NAME = "noProfileObject";
    private static final String TEST1_BEAN_NAME = "test1Object";
    private static final String TEST2_BEAN_NAME = "test2Object";

    private static final String SIMPLE_STRING_BEAN_NAME = "simpleString";


    @Test
    public void applyInitializerInDefaultLocation() {
        final ConfigurableApplicationContext ctx = doLoadApplicationContext(SIMPLE_PROFILES_CONTEXT,
                new ProfilesActivationApplicationInitializer());
        try {
            assertContain(ctx, NO_PROFILE_BEAN_NAME, TEST1_BEAN_NAME);
            assertDoesNotContain(ctx, TEST2_BEAN_NAME);
        } finally {
            ctx.close();
        }
    }

    @Test
    public void applyInitializerNoFileFound() {
        final ConfigurableApplicationContext ctx = doLoadApplicationContext(SIMPLE_PROFILES_CONTEXT,
                createInitializer(DUMMY_PROPERTIES_PATH));
        try {
            assertContain(ctx, NO_PROFILE_BEAN_NAME);
            assertDoesNotContain(ctx, TEST1_BEAN_NAME, TEST2_BEAN_NAME);
        } finally {
            ctx.close();
        }
    }

    @Test
    public void applyInitializerOverrideDefaultBean() {
        final ConfigurableApplicationContext ctx = doLoadApplicationContext(OVERRIDING_PROFILES_CONTEXT,
                new ProfilesActivationApplicationInitializer());
        try {
            assertSimpleString(ctx, "test1");
        } finally {
            ctx.close();
        }
    }

    @Test
    public void applyInitializerWithNoLocationUseDefaultBean() {
        final ConfigurableApplicationContext ctx = doLoadApplicationContext(OVERRIDING_PROFILES_CONTEXT,
                createInitializer(DUMMY_PROPERTIES_PATH));
        try {
            assertSimpleString(ctx, "noProfile");
        } finally {
            ctx.close();
        }
    }

    @Test
    public void applyInitializerWithSeveralProfilesTakeLast() {
        final ConfigurableApplicationContext ctx = doLoadApplicationContext(OVERRIDING_PROFILES_CONTEXT,
                createInitializer("META-INF/test1-test2.properties"));
        try {
            assertSimpleString(ctx, "test2");
        } finally {
            ctx.close();
        }
    }


    private ConfigurableApplicationContext doLoadApplicationContext(String path,
                                                                      ProfilesActivationApplicationInitializer initializer) {

        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext();
        ctx.setConfigLocation(path);

        // Apply the initializer
        initializer.initialize(ctx);

        ctx.refresh();

        return ctx;
    }

    private ProfilesActivationApplicationInitializer createInitializer(String path) {
        final TestableProfilesActivationApplicationInitializer initializer =
                new TestableProfilesActivationApplicationInitializer();
        initializer.setPropertiesPath(path);
        return initializer;
    }

    private void assertSimpleString(ApplicationContext ctx, String expected) {
        assertEquals("Wrong bean definition for [" + SIMPLE_STRING_BEAN_NAME + "]", expected,
                ctx.getBean(SIMPLE_STRING_BEAN_NAME, String.class));
    }

    /**
     * Asserts that the given context contains the specified bean name(s).
     *
     * @param context the given context
     * @param beanNames the bean name(s) to assert
     */
    private void assertContain(ApplicationContext context, String... beanNames) {
        for (String beanName : beanNames) {
            Assert.assertTrue("The application context does not contain [" + beanName + "]",
                    context.containsBean(beanName));
        }
    }

    /**
     * Asserts that the given context does not contain the specified bean name(s).
     *
     * @param context the given context
     * @param beanNames the bean name(s) to assert
     */
    private void assertDoesNotContain(ApplicationContext context, String... beanNames) {
        for (String beanName : beanNames) {
            Assert.assertFalse("The application context contains [" + beanName + "]",
                    context.containsBean(beanName));
        }
    }

}
