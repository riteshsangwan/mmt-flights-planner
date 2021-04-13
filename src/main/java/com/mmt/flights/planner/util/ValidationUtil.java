package com.mmt.flights.planner.util;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Application level validation utility class
 *
 * @author ritesh
 * @version 1.0.0
 */
public class ValidationUtil {

    /**
     * We don't want anyone to instantiate this
     */
    private ValidationUtil() {
    }

    /**
     * Assert that the arguments are not null
     *
     * @param args the arguments to check for nullability
     * @throws IllegalArgumentException if argument validation failed
     */
    public static void assertNotNull(Object... args) {
        final AtomicInteger index = new AtomicInteger();
        for (Object arg : args) {
            assertNotNull(arg, String.format("argument at index {%d} must not be null", index.incrementAndGet()));
        }
    }

    /**
     * Assert that variable is valid
     *
     * @param arg     the argument to validate
     * @param message the message if argument is invalid
     * @throws IllegalArgumentException if argument is not valid
     */
    public static void assertValid(String arg, String message) {
        if (StringUtils.isBlank(arg)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert that variable is not null
     *
     * @param arg     the argument to validate
     * @param message the message if argument is invalid
     * @throws IllegalArgumentException if argument validation failed
     */
    public static void assertNotNull(Object arg, String message) {
        if (arg == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert that variable is null
     *
     * @param arg     the argument to validate
     * @param message the message if argument is invalid
     * @throws IllegalArgumentException if argument validation failed
     */
    public static void assertNull(Object arg, String message) {
        if (arg != null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Assert the boolean condition to be true
     *
     * @param condition the condition to be true
     * @param message   the error message
     * @throws IllegalArgumentException if the condition is not true
     */
    public static void assertTrue(Boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }
}
