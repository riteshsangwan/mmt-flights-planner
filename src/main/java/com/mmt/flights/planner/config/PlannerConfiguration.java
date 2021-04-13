package com.mmt.flights.planner.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Root configuration class for application
 *
 * @author ritesh
 * @version 1.0.0
 */
@Configuration
@ComponentScan(basePackages = "com.mmt.flights.planner")
@EnableAutoConfiguration
public class PlannerConfiguration implements WebMvcConfigurer, ApplicationContextAware {

    /**
     * static class level logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PlannerConfiguration.class);

    /**
     * A path parameter or query parameter key that maps to {@link org.springframework.http.MediaType#APPLICATION_JSON_VALUE}
     */
    private static final String MEDIA_TYPE_JSON_KEY = "json";

    /**
     * The context for this bean spring context
     */
    private ApplicationContext applicationContext;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * Configure custom content negotiation
     * The app only supports application/json content negotiation
     *
     * @param configurer the content negotiation configurer
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorParameter(true)
                .mediaType(MEDIA_TYPE_JSON_KEY, MediaType.APPLICATION_JSON)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .ignoreAcceptHeader(true)
                .useRegisteredExtensionsOnly(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("*")
                .maxAge(900)    // 15 minutes instead of 30 minutes
                .allowedOrigins("*");
    }
}
