package com.mmt.flights.planner.router.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmt.flights.planner.api.ApiClientErrorDetail;
import com.mmt.flights.planner.service.exception.ClientErrorException;
import com.mmt.flights.planner.util.ValidationUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

/**
 * Abstract base controller for all application level controllers
 *
 * @author ritesh
 * @version 1.0.0
 */
public abstract class AbstractController implements InitializingBean {

    /**
     * The jackson object mapper to write json
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Validate this bean
     *
     * @throws Exception if validation failed
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        ValidationUtil.assertNotNull(objectMapper);
        afterPropertiesSetInternal();
    }

    /**
     * Implementations must implement this to add custom validation logic
     *
     * @throws Exception if validation failed
     */
    protected abstract void afterPropertiesSetInternal() throws Exception;

    /**
     * Log the method entry
     *
     * @param logger the logger to use for logging
     * @param name   the class and method name
     * @param args   any additional arguments
     */
    protected void logEntry(Logger logger, String name, Object... args) {
        try {
            String json = objectMapper.writeValueAsString(args);
            logger.debug("entering method - {}, args - {}", name, json);
        } catch (JsonProcessingException ignore) {
            logger.error("error logging method request parameters method - {}, args - {}", name, args, ignore);
        }
    }

    /**
     * Assert that the provided object argument is not null
     *
     * @param arg     the object argument to be validated
     * @param name    the name of the argument
     * @param message the error message if argument is not valid
     * @param code    the error code if argument is not valid
     * @throws ClientErrorException if argument validation failed
     */
    protected void assertNotNull(Object arg, String name, String message, String code) throws ClientErrorException {
        try {
            ValidationUtil.assertNotNull(arg, message);
        } catch (IllegalArgumentException ex) {
            ApiClientErrorDetail errorDetail = ApiClientErrorDetail.builder()
                    .field(name)
                    .message(String.format("expected %s value to be non null, but received %s", name, arg))
                    .build();
            throw new ClientErrorException(code, message, Arrays.asList(errorDetail), ex);
        }
    }

    /**
     * Assert that the provided boolean condition is true
     *
     * @param arg     the boolean argument to be validated
     * @param name    the name of argument
     * @param message the error message if argument is not valid
     * @param code    the error code if argument is not valid
     * @throws ClientErrorException if argument validation failed
     */
    protected void assertTrue(Boolean arg, String name, String message, String code) throws ClientErrorException {
        try {
            ValidationUtil.assertTrue(arg, message);
        } catch (IllegalArgumentException ex) {
            ApiClientErrorDetail errorDetail = ApiClientErrorDetail.builder()
                    .field(name)
                    .message(String.format("expected %s value to be valid. %s", name, message))
                    .build();
            throw new ClientErrorException(code, message, Arrays.asList(errorDetail), ex);
        }
    }

    /**
     * Assert that the provided object argument is null
     *
     * @param arg     the object argument to be validated
     * @param name    the name of argument
     * @param message the error message if argument is not valid
     * @param code    the error code if argument is not valid
     * @throws ClientErrorException if argument validation failed
     */
    protected void assertNull(Object arg, String name, String message, String code) throws ClientErrorException {
        try {
            ValidationUtil.assertNull(arg, message);
        } catch (IllegalArgumentException ex) {
            ApiClientErrorDetail errorDetail = ApiClientErrorDetail.builder()
                    .field(name)
                    .message(String.format("expected %s value to be null, but received %s", name, arg))
                    .build();
            throw new ClientErrorException(code, message, Arrays.asList(errorDetail), ex);
        }
    }

    /**
     * Assert that the provided string argument is a valid string
     *
     * @param arg     the string argument to be validated
     * @param name    the name of argument
     * @param message the error message if argument is not valid
     * @param code    the error code if argument is not valid
     * @throws ClientErrorException if argument validation failed
     */
    protected void assertValid(String arg, String name, String message, String code) throws ClientErrorException {
        try {
            ValidationUtil.assertValid(arg, message);
        } catch (IllegalArgumentException ex) {
            ApiClientErrorDetail errorDetail = ApiClientErrorDetail.builder()
                    .field(name)
                    .message(String.format("expected %s value to be non null/blank, but received %s", name, arg))
                    .build();
            throw new ClientErrorException(code, message, Arrays.asList(errorDetail), ex);
        }
    }
}