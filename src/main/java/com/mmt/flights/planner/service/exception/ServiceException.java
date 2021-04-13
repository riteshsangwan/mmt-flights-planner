package com.mmt.flights.planner.service.exception;

/**
 * Generic service exceptions thrown by any service implementations
 * This exception is checked exception and must be declared in throws clause
 * The more detailed error cause can be identified by the provided code or the exception cause
 *
 * @author ritesh
 * @version 1.0.0
 */
public class ServiceException extends RuntimeException {

    /**
     * The error code
     */
    private final String code;

    /**
     * Create instance of external api exception with specified message
     *
     * @param code    the error code
     * @param message the exception message
     */
    public ServiceException(String code, String message) {
        this(code, message, null);
    }


    /**
     * Create instance of external api exception with specified code, message and cause
     *
     * @param code    the code for this exception
     * @param message the exception message
     * @param cause   the cause for this exception
     */
    public ServiceException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * Get the value of code
     *
     * @return the value of code
     */
    public String getCode() {
        return code;
    }
}
