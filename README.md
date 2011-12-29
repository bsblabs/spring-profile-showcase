# Spring profile showcase

The purpose of this showcase is to demonstrate how easy it is to customize how a Spring application context starts. See [this blog post](http://blog.springsource.com/2011/02/15/spring-3-1-m1-unified-property-management/) for an excellent introduction.

Our goal is to enable a set of profile(s) based on a properties file loaded from the classpath. This implementation loads from META-INF/application-context.xml but it could be any other location you may desire.

# Building from Source

Clone the git repository using the URL on the Github home page:

    $ git clone git@github.com:bsblabs/spring-profile-showcase.git
    $ cd spring-profile-showcase

## Command Line

Use Maven, then on the command line:

    $ mvn install

Make sure you have access to the central repository.

## Usage

To use this showcase, add the following to your web.xml

    <context-param>
        <param-name>contextInitializerClasses</param-name>
        <param-value>com.bsb.showcase.spring.profile.ProfilesActivationApplicationInitializer</param-value>
    </context-param>

And add a file located in `META-INF/application-context.properties` with the profiles you would like to enable.

    spring.profiles.active=env1,service2

Because the property source is added last, any system or environment property will take precedence. This basically means that even when this is active you can start your application (server) with `-Dspring.profiles.active=env1,service5` to override the default behavior.

